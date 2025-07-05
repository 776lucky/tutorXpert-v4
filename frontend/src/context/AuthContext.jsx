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

    const enrichedUser = {
      id: userData.id, // This might be undefined
      email: userData.email,
      role: role,
      userType: role,
      fullName: `${profile.first_name || ""} ${profile.last_name || ""}`.trim(),
      avatarUrl: profile.avatar_url || null,
      subjects: profile.subjects || [],
      hourlyRate: profile.hourly_rate || null,
      lat: profile.lat ?? null,
      lng: profile.lng ?? null,
      address: profile.address || "",
      isLoggedIn: true,
      token: token,
    };

    localStorage.setItem("token", token);
    localStorage.setItem("user", JSON.stringify(enrichedUser));
    if (enrichedUser.id != null) {
      localStorage.setItem("user_id", enrichedUser.id.toString());
    }
    setUser(enrichedUser);
  };

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    localStorage.removeItem("user_id");

    // 可选：清除额外缓存字段
    localStorage.removeItem("selectedSlot");
    localStorage.removeItem("draftMessage");

    setUser(null);
  };

  const isAuthenticated = !!user;

  return (
    <AuthContext.Provider value={{ user, setUser, login, logout, isAuthenticated }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
