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
import Cookies from "js-cookie";

var stompClient = null;
const ChatPage = () => {
  const [tab, setTab] = useState("");
  const [chatMessages, setChatMessages] = useState([]);
  const [open, setOpen] = useState(false);
  const [newChatName, setNewChatName] = useState("");
  const [selectedUsers, setSelectedUsers] = useState([]);
  const [allUsers, setAllUsers] = useState([]);
  const [userData, setUserData] = useState({
    username: Cookies.get("Username"),
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
    getAllChatRoom();
    getAllUsers();
  }, []);

  useEffect(() => {
    connect();
  }, [chatRoom]);

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
      selectedUsers.push(
        allUsers.find((u) => u.username === userData.username).id
      );
      const response = await axios.post("/chat/creat", {
        name: newChatName,
        typeChat: "USER_CHAT",
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

  const getUsernames = (userIds) => {
    return userIds.map((id) => {
      const user = allUsers.find((user) => user.id === id);
      return user ? user.username : "";
    });
  };

  return (
    <>
      <NavBar />
      <Container sx={{ height: "85vh" }}>
        <Box display="flex" sx={{ height: "100%" }}>
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
          <Box
            flexGrow={1}
            display="flex"
            flexDirection="column"
            padding="16px"
            sx={{ height: "100%" }}
          >
            <Box
              flexGrow={1}
              overflow="auto"
              sx={{
                mb: 2,
                maxHeight: "calc(100% - 68px)",
                "&::-webkit-scrollbar": {
                  width: "8px",
                },
                "&::-webkit-scrollbar-track": {
                  background: "#f1f1f1",
                },
                "&::-webkit-scrollbar-thumb": {
                  background: "#888",
                  borderRadius: "4px",
                },
                "&::-webkit-scrollbar-thumb:hover": {
                  background: "#555",
                },
              }}
            >
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
            </Box>
            <Box
              display="flex"
              sx={{
                mt: "auto",
                paddingTop: "16px",
                borderTop: "1px solid #eee",
              }}
            >
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
                sx={{ ml: 2 }}
              >
                Send
              </Button>
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
              renderValue={(selected) => getUsernames(selected).join(", ")}
            >
              {allUsers
                .filter((u) => u.username !== userData.username)
                .map((user) => (
                  <MenuItem key={user.id} value={user.id}>
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
