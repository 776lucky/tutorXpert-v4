import React, { useEffect, useState } from "react";
import axios from "axios";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { useNavigate } from "react-router-dom";
import { useToast } from "@/components/ui/use-toast";
import AddressAutoComplete from "@/components/AddressAutoComplete";

const EditProfilePage = () => {
    const [formData, setFormData] = useState({});
    const { toast } = useToast();
    const navigate = useNavigate();

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                const res = await axios.get(`${import.meta.env.VITE_API_BASE_URL}/users/profile`, {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem("token")}`,
                    },
                });

                setFormData({
                    ...res.data.profile,
                    ...res.data.tutor,
                    ...res.data.user,
                });
            } catch (err) {
                console.error("Failed to fetch profile", err);
            }
        };
        fetchProfile();
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: name === "hourlyRate" ? Number(value) : value,
        }));
    };

    const handleSave = async () => {
        const auth = JSON.parse(localStorage.getItem("auth") || "{}");
        if (!auth.token) {
            console.error("No token found, please login again.");
            return;
        }

        const payload = {
            bio: formData.bio,
            expertise: formData.expertise,
            hourlyRate: formData.hourlyRate,
            phoneNumber: formData.phoneNumber,
            educationLevel: formData.educationLevel,
            address: formData.address,
            subjects: Array.isArray(formData.subjects)
                ? formData.subjects.join(",")
                : formData.subjects ?? "",
        };

        try {
            await axios.put(`${import.meta.env.VITE_API_BASE_URL}/users/profile`, payload, {
                headers: { Authorization: `Bearer ${auth.token}` },
            });

            toast({
                title: "Edited successfully!",
                duration: 2000,
                className: "bg-green-500 text-white shadow-md",
            });

            const updatedProfile = await axios.get(`${import.meta.env.VITE_API_BASE_URL}/users/profile`, {
                headers: { Authorization: `Bearer ${auth.token}` },
            });
            localStorage.setItem("profile", JSON.stringify(updatedProfile.data));

            navigate("/dashboard/profile");  // ✅ 保存后跳转
        } catch (err) {
            console.error("Failed to save profile:", err);
        }
    };



    return (
        <div className="p-6">
            <h2 className="text-2xl mb-4">Edit My Profile</h2>
            <div className="space-y-4">
                <div>
                    <label className="block font-medium mb-1">Bio</label>
                    <Input
                        name="bio"
                        value={formData.bio || ""}
                        onChange={handleChange}
                        placeholder="Bio"
                    />
                </div>
                <div>
                    <label className="block font-medium mb-1">Expertise</label>
                    <Input
                        name="expertise"
                        value={formData.expertise || ""}
                        onChange={handleChange}
                        placeholder="Expertise"
                    />
                </div>
                <div>
                    <label className="block font-medium mb-1">Hourly Rate</label>
                    <Input
                        type="number"
                        name="hourlyRate"
                        value={formData.hourlyRate || ""}
                        onChange={handleChange}
                        placeholder="Hourly Rate"
                    />
                </div>
                <div>
                    <label className="block font-medium mb-1">Education Level</label>
                    <Input
                        name="educationLevel"
                        value={formData.educationLevel || ""}
                        onChange={handleChange}
                        placeholder="Education Level"
                    />
                </div>
                <div>
                    <label className="block font-medium mb-1">Phone Number</label>
                    <Input
                        name="phoneNumber"
                        value={formData.phoneNumber || ""}
                        onChange={handleChange}
                        placeholder="Phone Number"
                    />
                </div>
                <div>
                    <label className="block font-medium mb-1">Address</label>
                    <AddressAutoComplete form={formData} setForm={setFormData} />
                </div>
                <div className="flex gap-4">
                    <Button onClick={handleSave}>Save</Button>
                    <Button variant="outline" onClick={() => navigate("/dashboard/profile")}>Cancel</Button>
                </div>
            </div>
        </div>
    );
};

export default EditProfilePage;
