import React, { useEffect, useState } from "react";
import axios from "axios";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { useToast } from "@/components/ui/use-toast";
import { useNavigate } from "react-router-dom";
import AddressAutoComplete from "@/components/AddressAutoComplete";

const EditTutorProfilePage = () => {
    const [form, setForm] = useState({});
    const { toast } = useToast();
    const navigate = useNavigate();

    useEffect(() => {
        axios.get(`${import.meta.env.VITE_API_BASE_URL}/users/profile/tutor`, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
        })
            .then((res) => setForm(res.data))
            .catch((err) => console.error("获取资料失败", err));
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setForm((prev) => ({
            ...prev,
            [name]: name === "hourlyRate" || name === "yearsOfExperience" ? Number(value) : value,
        }));
    };

    const handleSave = async () => {
        try {
            await axios.post(`${import.meta.env.VITE_API_BASE_URL}/users/profile/tutor`, form, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
            });
            toast({
                title: "保存成功",
                duration: 2000,
                className: "bg-green-500 text-white",
            });
            navigate("/dashboard/profile");
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
            <h2 className="text-xl font-semibold">Edit Tutor Profile</h2>

            <div>
                <label className="block mb-1">Name</label>
                <Input name="name" value={form.name || ""} onChange={handleChange} placeholder="Enter name" />
            </div>

            <div>
                <label className="block mb-1">Email</label>
                <Input name="email" value={form.email || ""} disabled placeholder="Your email" />
            </div>

            <div>
                <label className="block mb-1">Bio</label>
                <Input name="bio" value={form.bio || ""} onChange={handleChange} placeholder="Short bio" />
            </div>

            <div>
                <label className="block mb-1">Expertise</label>
                <Input name="expertise" value={form.expertise || ""} onChange={handleChange} placeholder="Subjects you specialize in" />
            </div>

            <div>
                <label className="block mb-1">Hourly Rate</label>
                <Input name="hourlyRate" value={form.hourlyRate || ""} onChange={handleChange} type="number" placeholder="e.g., 30" />
            </div>

            <div>
                <label className="block mb-1">Years of Experience</label>
                <Input name="yearsOfExperience" value={form.yearsOfExperience || ""} onChange={handleChange} type="number" placeholder="e.g., 5" />
            </div>

            <div>
                <label className="block mb-1">Certifications</label>
                <Input name="certifications" value={form.certifications || ""} onChange={handleChange} placeholder="Certifications or qualifications" />
            </div>

            <div>
                <label className="block mb-1">Avatar URL</label>
                <Input name="avatarUrl" value={form.avatarUrl || ""} onChange={handleChange} placeholder="Link to profile picture" />
            </div>

            <div>
                <label className="block mb-1">Teaching Modes</label>
                <Input name="teachingModes" value={form.teachingModes || ""} onChange={handleChange} placeholder="e.g., Online / Offline" />
            </div>

            <div>
                <label className="block mb-1">Tags</label>
                <Input name="tags" value={form.tags || ""} onChange={handleChange} placeholder="e.g., Primary Math, English" />
            </div>

            <AddressAutoComplete form={form} setForm={setForm} />

            <div className="flex gap-4 pt-2">
                <Button onClick={handleSave}>Save</Button>
                <Button variant="outline" onClick={() => setIsEditing(false)}>Cancel</Button>

            </div>
        </div>
    );
};

export default EditTutorProfilePage;
