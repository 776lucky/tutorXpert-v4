import React, { useEffect, useState } from "react";
import axios from "axios";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { useNavigate } from "react-router-dom";
import { useToast } from "@/components/ui/use-toast";
import AddressAutoComplete from "@/components/AddressAutoComplete";

const EditStudentProfilePage = () => {
    const [formData, setFormData] = useState({});
    const navigate = useNavigate();
    const { toast } = useToast();

    useEffect(() => {
        axios
            .get(`${import.meta.env.VITE_API_BASE_URL}/users/profile/student`, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
            })
            .then((res) => setFormData(res.data))
            .catch((err) => console.error("Failed to fetch student profile", err));
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleSave = async () => {
        try {
            await axios.post(
                `${import.meta.env.VITE_API_BASE_URL}/users/profile/student`,
                formData,
                {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem("token")}`,
                    },
                }
            );

            toast({
                title: "Update Success!",
                duration: 2000,
                className: "bg-green-500 text-white",
            });

            navigate("/dashboard/profile");
        } catch (err) {
            console.error("保存失败:", err);
        }
    };

    return (
        <div className="p-6">
            <h2 className="text-2xl mb-4">Edit Student Profile</h2>
            <div className="space-y-4">
                <Input
                    name="name"
                    value={formData.name || ""}
                    onChange={handleChange}
                    placeholder="name"
                />
                <Input
                    name="email"
                    value={formData.email || ""}
                    disabled
                    placeholder="email"
                />
                <Input
                    name="educationLevel"
                    value={formData.educationLevel || ""}
                    onChange={handleChange}
                    placeholder="educationLevel"
                />
                <Input
                    name="subjectNeed"
                    value={formData.subjectNeed || ""}
                    onChange={handleChange}
                    placeholder="subjectNeed"
                />
                <Input
                    name="briefDescription"
                    value={formData.briefDescription || ""}
                    onChange={handleChange}
                    placeholder="briefDescription"
                />
                <AddressAutoComplete form={formData} setForm={setFormData} />

                <div className="flex gap-4">
                    <Button onClick={handleSave}>Save</Button>
                    <Button variant="outline" onClick={() => navigate("/dashboard/profile")}>
                        Cancle
                    </Button>
                </div>
            </div>
        </div>
    );
};

export default EditStudentProfilePage;
