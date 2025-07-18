import React, { useEffect, useState } from "react";
import axios from "axios";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { useNavigate } from "react-router-dom";
import { useToast } from "@/components/ui/use-toast";
import AddressAutoComplete from "@/components/AddressAutoComplete";

const EditStudentProfilePage = () => {
    const [form, setForm] = useState({});
    const navigate = useNavigate();
    const { toast } = useToast();

    useEffect(() => {
        axios.get(`${import.meta.env.VITE_API_BASE_URL}/users/profile/student`, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
        })
            .then((res) => setForm(res.data))
            .catch((err) => console.error("获取学生资料失败", err));
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setForm((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleSave = async () => {
        try {
            await axios.post(`${import.meta.env.VITE_API_BASE_URL}/users/profile/student`, form, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
            });
            toast({
                title: "保存成功",
                duration: 2000,
                className: "bg-green-500 text-white",
            });

            navigate(`/dashboard/profile`);
        } catch (err) {
            console.error("保存失败", err);
            toast({
                title: "保存失败",
                description: "请稍后重试",
                className: "bg-red-500 text-white",
            });
        }
    };

    return (
        <div className="max-w-2xl mx-auto py-6 space-y-5">
            <h2 className="text-xl font-semibold">Edit Student Profile</h2>

            <div>
                <label className="block mb-1">Name</label>
                <Input name="name" value={form.name || ""} onChange={handleChange} placeholder="Enter name" />
            </div>

            <div>
                <label className="block mb-1">Email</label>
                <Input name="email" value={form.email || ""} disabled />
            </div>

            <div>
                <label className="block mb-1">Bio</label>
                <Input name="bio" value={form.bio || ""} onChange={handleChange} placeholder="Short introduction" />
            </div>

            <div>
                <label className="block mb-1">Education Level</label>
                <Input name="educationLevel" value={form.educationLevel || ""} onChange={handleChange} />
            </div>

            <div>
                <label className="block mb-1">Subject Need</label>
                <Input name="subjectNeed" value={form.subjectNeed || ""} onChange={handleChange} />
            </div>

            <div>
                <label className="block mb-1">Brief Description</label>
                <Input name="briefDescription" value={form.briefDescription || ""} onChange={handleChange} />
            </div>

            <div>
                <label className="block mb-1">Address Area</label>
                <Input name="addressArea" value={form.addressArea || ""} onChange={handleChange} />
            </div>

            <div>
                <label className="block mb-1">Avatar URL</label>
                <Input name="avatarUrl" value={form.avatarUrl || ""} onChange={handleChange} />
            </div>

            <AddressAutoComplete form={form} setForm={setForm} />

            <div className="flex gap-4 pt-2">
                <Button onClick={handleSave}>Save</Button>
                <Button variant="outline" onClick={() => navigate("/dashboard/profile")}>Cancel</Button>
            </div>
        </div>
    );
};

export default EditStudentProfilePage;
