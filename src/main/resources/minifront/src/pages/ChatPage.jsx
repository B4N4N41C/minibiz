import React, { useEffect, useState } from "react";
import { over } from "stompjs";
import SockJS from "sockjs-client";
import {
	Container,
	List,
	ListItem,
	ListItemText,
	TextField,
	Button,
	Box,
	Dialog,
	DialogActions,
	DialogContent,
	DialogTitle,
	MenuItem,
	Select,
	InputLabel,
	FormControl,
} from "@mui/material";
import NavBar from "../components/NavBar.jsx";
import axios from "axios";

var stompClient = null;
const ChatPage = () => {
	const [tab, setTab] = useState("");
	const [chatMessages, setChatMessages] = useState([]);
	const [open, setOpen] = useState(false);
	const [newChatName, setNewChatName] = useState("");
	const [selectedUsers, setSelectedUsers] = useState([]);
	const [allUsers, setAllUsers] = useState([]);
	const [userData, setUserData] = useState({
		username: localStorage.getItem("username"),
		receivername: "",
		connected: false,
		message: "",
	});
	let [chatRoom, setChatRoom] = useState([]);
	useEffect(() => {
		console.log(userData);
	}, [userData]);

	useEffect(() => {
		console.log("конект");
		connect();
		getAllChatRoom();
		getAllUsers();
	}, []);

	useEffect(() => {
		if (tab) {
			const selectedChat = chatRoom.find((c) => c.name === tab);
			if (selectedChat) {
				setChatMessages(selectedChat.messages);
			}
		}
	}, [tab, chatRoom]);

	const getAllChatRoom = async () => {
		try {
			const { data } = await axios.get("/getAllChatRoom/" + userData.username);
			setChatRoom(Array.isArray(data) ? data : [data]);
		} catch (error) {
			console.error("Error fetching tasks:", error);
		}
	};

	const getAllUsers = async () => {
		try {
			const { data } = await axios.get("/users");
			setAllUsers(data);
		} catch (error) {
			console.error("Error fetching users:", error);
		}
	};

	const connect = () => {
		let Sock = new SockJS("http://localhost:8080/ws");
		stompClient = over(Sock);
		stompClient.connect({}, onConnected, onError);
	};

	const onConnected = () => {
		setUserData({ ...userData, connected: true });
		[...chatRoom].map((chat) => {
			stompClient.subscribe("/chatroom/" + chat.id, onPrivateMessage);
		});
		userJoin();
	};

	const userJoin = () => {
		var chatMessage = {
			senderName: userData.username,
			status: "JOIN",
		};
		stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
	};

	const onPrivateMessage = (payload) => {
		console.log(payload);
		var payloadData = JSON.parse(payload.body);
		const updatedChatRoom = chatRoom.map((chat) => {
			if (chat.id === payloadData.chatRoom) {
				return {
					...chat,
					messages: [...chat.messages, payloadData],
				};
			}
			return chat;
		});
		setChatRoom(updatedChatRoom);

		if (payloadData.chatRoom === chatRoom.find((c) => c.name === tab)?.id) {
			setChatMessages((prevMessages) => [...prevMessages, payloadData]);
		}
	};

	const onError = (err) => {
		console.log(err);
	};

	const handleMessage = (event) => {
		const { value } = event.target;
		setUserData({ ...userData, message: value });
	};

	const sendPrivateValue = () => {
		if (stompClient) {
			const chatMessage = {
				senderName: userData.username,
				chatRoom: chatRoom.find((c) => c.name === tab).id,
				message: userData.message,
				status: "MESSAGE",
			};

			stompClient.send("/app/private-message", {}, JSON.stringify(chatMessage));
			setChatMessages((prevMessages) => [...prevMessages, chatMessage]);
			setUserData({ ...userData, message: "" });
		}
	};

	const handleOpenModal = () => {
		setOpen(true);
	};

	const handleCloseModal = () => {
		setOpen(false);
		setNewChatName("");
		setSelectedUsers([]);
	};

	const handleCreateChat = async () => {
		try {
			const response = await axios.post("/createChat", {
				name: newChatName,
				creator: userData.username,
				users: selectedUsers,
			});
			if (response.status === 200) {
				getAllChatRoom();
				handleCloseModal();
			}
		} catch (error) {
			console.error("Error creating chat:", error);
		}
	};

	return (
		<>
			<NavBar />
			<Container sx={{ height: "85vh" }}>
				<Box display="flex" sx={{ height: "85vh" }}>
					<Box width="200px" borderRight="1px solid #ccc">
						<Button
							variant="contained"
							color="primary"
							onClick={handleOpenModal}
							sx={{ mb: 2 }}
						>
							Добавить чат
						</Button>
						<List>
							{[...chatRoom].map((chat, index) => (
								<ListItem
									button
									onClick={() => setTab(chat.name)}
									selected={tab === chat.name}
									key={index}
								>
									<ListItemText primary={chat.name} />
								</ListItem>
							))}
						</List>
					</Box>
					<Box flexGrow={1} padding="16px">
						<Box>
							<List>
								{chatMessages.map((message, msgIndex) => (
									<ListItem
										key={msgIndex}
										alignItems="flex-start"
										sx={{ display: "block" }}
									>
										<div
											style={{
												display: "flex",
												flexDirection:
													message.senderName === userData.username
														? "row-reverse"
														: "row",
												justifyContent:
													message.senderName === userData.username
														? "flex-end"
														: "flex-start",
												marginBottom: "8px",
											}}
										>
											{message.senderName !== userData.username && (
												<Box marginRight="8px" style={{ fontWeight: "bold" }}>
													{message.senderName}
												</Box>
											)}
											<Box
												padding="8px 12px"
												borderRadius="8px"
												backgroundColor={
													message.senderName === userData.username
														? "#dcf8c6"
														: "#fff"
												}
												boxShadow="0 1px 3px rgba(0,0,0,0.1)"
											>
												{message.message}
											</Box>
											{message.senderName === userData.username && (
												<Box marginLeft="8px" style={{ fontWeight: "bold" }}>
													{message.senderName}
												</Box>
											)}
										</div>
									</ListItem>
								))}
							</List>
							<Box display="flex" marginTop="16px">
								<TextField
									fullWidth
									placeholder="Enter the message"
									value={userData.message}
									onChange={handleMessage}
								/>
								<Button
									variant="contained"
									color="primary"
									onClick={sendPrivateValue}
								>
									Send
								</Button>
							</Box>
						</Box>
					</Box>
				</Box>
			</Container>
			<Dialog open={open} onClose={handleCloseModal}>
				<DialogTitle>Добавить новый чат</DialogTitle>
				<DialogContent>
					<TextField
						autoFocus
						margin="dense"
						label="Название чата"
						fullWidth
						value={newChatName}
						onChange={(e) => setNewChatName(e.target.value)}
					/>
					<FormControl fullWidth margin="dense">
						<InputLabel>Выберите пользователей</InputLabel>
						<Select
							multiple
							value={selectedUsers}
							onChange={(e) => setSelectedUsers(e.target.value)}
							renderValue={(selected) => selected.join(", ")}
						>
							{allUsers.map((user) => (
								<MenuItem key={user.id} value={user.username}>
									{user.username}
								</MenuItem>
							))}
						</Select>
					</FormControl>
				</DialogContent>
				<DialogActions>
					<Button onClick={handleCloseModal} color="primary">
						Отмена
					</Button>
					<Button onClick={handleCreateChat} color="primary">
						Создать
					</Button>
				</DialogActions>
			</Dialog>
		</>
	);
};

export default ChatPage;
