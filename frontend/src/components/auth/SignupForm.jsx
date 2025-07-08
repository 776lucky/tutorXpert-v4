import React, { useState } from "react";
import axios from "axios";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import AddressAutoComplete from "@/components/AddressAutoComplete";
import { useNavigate } from "react-router-dom";

const SignupForm = () => {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    email: "",
    password: "",
    role: "",
    name: "",
  });

  const [addressInfo, setAddressInfo] = useState({
    address: "",
    lat: null,
    lng: null,
  });

  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      // 1. 注册用户 - 使用 query 参数
      const registerResponse = await axios.post(
          `${import.meta.env.VITE_API_BASE_URL}/auth/register`,
          {}, // 空的 request body
          {
            params: {
              email: form.email,
              password: form.password,
              role: form.role,
              name: form.name,
            }
          }
      );

      console.log("注册成功:", registerResponse.data);

      // 2. 自动登录获取 token
      const loginResponse = await axios.post(
          `${import.meta.env.VITE_API_BASE_URL}/auth/login`,
          {
            email: form.email,
            password: form.password,
          }
      );

      console.log("登录成功:", loginResponse.data);

      // 3. 保存 token
      localStorage.setItem('token', loginResponse.data.token);

      alert("注册成功！欢迎加入 GlowUpTutors！");
      navigate("/dashboard");

    } catch (err) {
      console.error("注册失败:", err);
      if (err.response?.data?.message) {
        alert(`注册失败: ${err.response.data.message}`);
      } else {
        alert("注册失败，请重试");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
      <form onSubmit={handleSubmit} className="space-y-4">
        {/* Role Select */}
        <div className="space-y-2">
          <label className="block text-sm font-medium">Sign Up As</label>
          <select
              value={form.role}
              onChange={(e) => setForm({ ...form, role: e.target.value })}
              className="w-full border rounded px-3 py-2 bg-background text-foreground"
              required
              disabled={loading}
          >
            <option value="">Select Role</option>
            <option value="tutor">Tutor</option>
            <option value="student">Student</option>
          </select>
        </div>

        {/* Personal Info */}
        <Input
            placeholder="Email"
            type="email"
            value={form.email}
            onChange={(e) => setForm({ ...form, email: e.target.value })}
            required
            disabled={loading}
        />
        <Input
            placeholder="Password"
            type="password"
            value={form.password}
            onChange={(e) => setForm({ ...form, password: e.target.value })}
            required
            disabled={loading}
        />
        <Input
            placeholder="Name"
            value={form.name}
            onChange={(e) => setForm({ ...form, name: e.target.value })}
            required
            disabled={loading}
        />

        {/* Address (Optional, for future use) */}
        <AddressAutoComplete form={addressInfo} setForm={setAddressInfo} />

        {/* Submit */}
        <Button type="submit" className="w-full" disabled={loading}>
          {loading ? "Signing Up..." : "Sign Up"}
        </Button>
      </form>
  );
};

export default SignupForm;