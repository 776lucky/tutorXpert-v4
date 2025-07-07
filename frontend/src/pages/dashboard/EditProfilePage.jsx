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
        try {
            const payload = {
                bio: formData.bio,
                expertise: formData.expertise,
                hourly_rate: formData.hourlyRate,
                education_level: formData.educationLevel,
                phone_number: formData.phoneNumber,
                address: formData.address,
            };

            await axios.put(`${import.meta.env.VITE_API_BASE_URL}/users/profile`, payload, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
            });

            toast({ title: "Saved successfully!", duration: 2000 });
            navigate("/dashboard/profile");
        } catch (err) {
            console.error("Failed to update profile", err);
            toast({ title: "Save failed", variant: "destructive" });
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
