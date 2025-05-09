import { create } from "zustand";

export const useStore = create((set) => ({
  moduleth: "",
  setModuleth: (newModuleth) => set({ moduleth: newModuleth }),
}));
