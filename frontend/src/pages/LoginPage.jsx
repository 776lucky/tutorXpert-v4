import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { useAuth } from "../context/AuthContext";

const LoginPage = () => {
  const toast = (options) => {
    console[options.variant === "destructive" ? "error" : "log"](options.description);
  };

  const navigate = useNavigate();
  const [formData, setFormData] = useState({ email: "", password: "" });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const { login } = useAuth();

  const baseUrl = import.meta.env.VITE_API_BASE_URL || "http://localhost:8000";

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);

    if (!formData.email || !formData.password) {
      toast({ description: "Please fill in all required fields.", variant: "destructive" });
      setIsSubmitting(false);
      return;
    }

    try {
      const response = await axios.post(
          `${baseUrl}/auth/login`,
          { email: formData.email, password: formData.password },
          { headers: { "Content-Type": "application/json" } }
      );
      console.log("Response:", response.data);

      const token = response.data?.token;
      if (!token) {
        throw new Error("Login endpoint did not return a token");
      }
      const userData = response.data.user || { id: null, email: formData.email };

      login(userData, String(token));

      toast({ description: "Login successful!" });
      navigate("/dashboard");
    } catch (error) {
      console.error("Login error:", error);
      const message =
          error.response?.data?.message || error.message || "Login failed, please try again.";
      toast({ description: message, variant: "destructive" });
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
      <div className="max-w-md mx-auto mt-10 p-6 bg-white rounded-lg shadow-md">
        <h2 className="text-2xl font-bold mb-6 text-center">Login</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label htmlFor="email" className="block text-sm font-medium text-gray-700">
              Email
            </label>
            <input
                type="email"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                placeholder="Enter your email"
                required
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
            />
          </div>
          <div>
            <label htmlFor="password" className="block text-sm font-medium text-gray-700">
              Password
            </label>
            <input
                type="password"
                id="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                placeholder="Enter your password"
                required
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
            />
          </div>
          <button
              type="submit"
              disabled={isSubmitting}
              className="w-full py-2 px-4 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:bg-gray-400"
          >
            {isSubmitting ? "Logging in..." : "Login"}
          </button>
        </form>
      </div>
  );
};

export default LoginPage;
