import React, { useEffect, useState } from "react";
import axios from "axios";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { motion } from "framer-motion";
import { Input } from "@/components/ui/input"; // ✅ 加在顶部
import { Button } from "@/components/ui/button";
import { useToast } from "@/components/ui/use-toast";
import AddressAutoComplete from "@/components/AddressAutoComplete";
import { useNavigate } from "react-router-dom";




const ProfilePage = () => {
  const { toast } = useToast();
  const [profile, setProfile] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [fetchError, setFetchError] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({});

  const storedUser = JSON.parse(localStorage.getItem("user") || "{}");
  const isTutor = storedUser?.role === "tutor";
  const navigate = useNavigate();

  useEffect(() => {
    if (!storedUser?.id) {
      setFetchError("No user ID found in local storage");
      setIsLoading(false);
      return;
    }

    const fetchProfile = async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        console.error("No token found, please login again.");
        setFetchError("Authentication token missing. Please log in again.");
        setIsLoading(false);
        return;
      }
      const res = await axios.get(`${import.meta.env.VITE_API_BASE_URL}/users/profile`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      try {
        const res = await axios.get(`${import.meta.env.VITE_API_BASE_URL}/users/profile`, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        });
        console.log(localStorage.getItem("token"));
        console.log("Fetched profile:", res.data);  // ✅ 添加这行
        setProfile(res.data);
      } catch (err) {
        console.error("Failed to fetch profile", err);  // ✅ 查看控制台是否报错
        setFetchError("Failed to load profile. Please try again later.");
      } finally {
        setIsLoading(false);
      }
    };
    

    fetchProfile();
  }, [storedUser?.id]);

  useEffect(() => {
    if (profile) {
      setFormData({ ...profile });
    }
  }, [profile]);

  const handleChange = (e) => {
    const { name, value } = e.target;
  
    setFormData((prev) => ({
      ...prev,
      [name]: name === "hourly_rate" ? Number(value) : value,
    }));
  };

  const handleSave = async () => {
    const auth = JSON.parse(localStorage.getItem("auth") || "{}");
    if (!auth.token) {
      console.error("No token found, please login again.");
      setFetchError("Authentication token missing. Please log in again.");
      setIsLoading(false);
      return;
    }

    const res = await axios.get(`${import.meta.env.VITE_API_BASE_URL}/users/profile`, {
      headers: { Authorization: `Bearer ${auth.token}` },
    });

    const payload = {
      ...formData,
      subjects: Array.isArray(formData.subjects)
          ? formData.subjects.join(",")
          : formData.subjects ?? "",
    };

    try {
      await axios.put(`${import.meta.env.VITE_API_BASE_URL}/users/profile`, payload, {
        headers: { Authorization: `Bearer ${auth.token}` },
      });

      setIsEditing(false);
      toast({
        title: "Edited successfully!",
        duration: 2000,
        className: "bg-green-500 text-white shadow-md",
      });

      const updatedProfile = await axios.get(`${import.meta.env.VITE_API_BASE_URL}/users/profile`, {
        headers: { Authorization: `Bearer ${auth.token}` },
      });
      setProfile(updatedProfile.data);
      localStorage.setItem("profile", JSON.stringify(updatedProfile.data));  // ✅ 存 profile
    } catch (err) {
      console.error("Failed to save profile:", err);
    }
  };





  const renderStudentProfile = () => (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
      <div>
        <label className="block mb-1 font-medium">First Name</label>
        <Input name="first_name" value={formData.first_name || ""} onChange={handleChange} className="text-white" />
      </div>
      <div>
        <label className="block mb-1 font-medium">Last Name</label>
        <Input name="last_name" value={formData.last_name || ""} onChange={handleChange} className="text-white" />
      </div>
      <div>
        <label className="block mb-1 font-medium">Education Level</label>
        <select
          name="education_level"
          value={formData.education_level || ""}
          onChange={handleChange}
          className="w-full bg-background text-white border border-input rounded-md px-3 py-2"
        >
          <option value="">Select</option>
          <option value="High School">High School</option>
          <option value="Bachelor">Bachelor</option>
          <option value="Master">Master</option>
          <option value="PhD">PhD</option>
        </select>
      </div>
      <div className="md:col-span-2">
        <label className="block mb-1 font-medium">Address</label>
        <AddressAutoComplete form={formData} setForm={setFormData} />
      </div>
    </div>
  );

  const renderTutorProfile = () => (
      <div className="grid grid-cols-1 gap-4">
        {/* tutor 部分 */}
        <div>
          <label className="block mb-1 font-medium">Self Introduction / Bio</label>
          <textarea
              name="bio"
              value={formData.bio || ""}
              onChange={handleChange}
              className="w-full bg-background text-white border border-input rounded-md px-3 py-2"
              rows={5}
          />
        </div>

        <div>
          <label className="block mb-1 font-medium">Expertise</label>
          <select
              name="expertise"
              value={formData.expertise || ""}
              onChange={handleChange}
              className="w-full bg-background text-white border border-input rounded-md px-3 py-2"
          >
            <option value="">Select Expertise</option>
            <option value="Math">Math</option>
            <option value="Physics">Physics</option>
            <option value="Chemistry">Chemistry</option>
            <option value="English">English</option>
            <option value="Programming">Programming</option>
          </select>
        </div>

        <div>
          <label className="block mb-1 font-medium">Hourly Rate ($/hr)</label>
          <Input
              type="number"
              name="hourly_rate"
              value={formData.hourly_rate || ""}
              onChange={handleChange}
              className="text-white"
          />
        </div>

        {/* profiles 部分 */}
        <div>
          <label className="block mb-1 font-medium">Education Level</label>
          <select
              name="education_level"
              value={formData.education_level || ""}
              onChange={handleChange}
              className="w-full bg-background text-white border border-input rounded-md px-3 py-2"
          >
            <option value="">Select</option>
            <option value="High School">High School</option>
            <option value="Bachelor">Bachelor</option>
            <option value="Master">Master</option>
            <option value="PhD">PhD</option>
          </select>
        </div>


        <div>
          <label className="block mb-1 font-medium">Address</label>
          <AddressAutoComplete form={formData} setForm={setFormData} />
        </div>
      </div>
  );


  






  

  return (
    <div className="min-h-screen bg-background text-foreground py-10 px-4 md:px-10">
      <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.5 }}>
        <Card className="glass-effect">
          <CardHeader>
            <CardTitle className="text-2xl">My Profile</CardTitle>
          </CardHeader>
          <CardContent>
            {isLoading ? (
                <p>Loading...</p>
            ) : fetchError ? (
                <p>Error</p>
            ) : (
                <>
                  {isEditing ? (
                      renderTutorProfile()
                  ) : (
                      <div className="space-y-4">
                        <div><strong>Bio:</strong> {profile.tutor.bio}</div>
                        <div><strong>Expertise:</strong> {profile.tutor.expertise}</div>
                        <div><strong>Hourly Rate:</strong> ${profile.tutor.hourlyRate}</div>
                        <div><strong>Education Level:</strong> {profile.profile?.educationLevel || "—"}</div>
                        <div><strong>Phone Number:</strong> {profile.profile?.phoneNumber || "—"}</div>
                        <div><strong>Address:</strong> {profile.profile.address}</div>
                      </div>
                  )}

                  <div className="mt-6 flex gap-4">
                    {isEditing ? (
                        <>
                          <Button onClick={handleSave}>Save</Button>
                          <Button variant="outline" onClick={() => setIsEditing(false)}>Cancel</Button>
                        </>
                    ) : (
                        <Button onClick={() => navigate("/dashboard/edit-profile")}>Edit Profile</Button>
                    )}
                  </div>
                </>
            )}
          </CardContent>

        </Card>
      </motion.div>
    </div>
  );
};

export default ProfilePage;
