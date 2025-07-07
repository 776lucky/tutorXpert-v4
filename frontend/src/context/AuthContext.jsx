import { createContext, useContext, useState, useEffect } from "react";
import axios from "axios";


const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const storedUser = localStorage.getItem("user");
    const token = localStorage.getItem("token");
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
    if (token) {
      axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
    }
  }, []);


  const login = (userData, token) => {
    const profile = userData.profile || {};
    const role = userData.role?.toLowerCase() || "student";

    const authData = {
      token: token,
      userId: userData.id,
      role: role,
      isLoggedIn: true,
    };

    localStorage.setItem("auth", JSON.stringify(authData));  // ✅ 只存 auth
    axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
    setUser(authData);
  };

  const logout = () => {
    localStorage.removeItem("auth");    // ✅ 只清 auth
    localStorage.removeItem("profile"); // ✅ 清除 profile（保持干净）

    // 清除其他缓存字段（保持你的逻辑）
    localStorage.removeItem("selectedSlot");
    localStorage.removeItem("draftMessage");

    setUser(null);
  };

// 初始化逻辑也改成统一读取 auth
  useEffect(() => {
    const storedAuth = localStorage.getItem("auth");
    if (storedAuth) {
      const authData = JSON.parse(storedAuth);
      setUser(authData);
      axios.defaults.headers.common["Authorization"] = `Bearer ${authData.token}`;
    }
  }, []);


  const isAuthenticated = !!user;

  return (
    <AuthContext.Provider value={{ user, setUser, login, logout, isAuthenticated }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
