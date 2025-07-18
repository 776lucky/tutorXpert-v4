import React, { useEffect, useState } from "react";
import axios from "axios";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { motion } from "framer-motion";
import { useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";

const ProfilePage = () => {
  const [profile, setProfile] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [fetchError, setFetchError] = useState(null);
  const navigate = useNavigate();

  const storedUser = JSON.parse(localStorage.getItem("user") || "{}");

  useEffect(() => {
    const fetchProfile = async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        setFetchError("Missing token");
        setIsLoading(false);
        return;
      }

      const role = storedUser?.role;
      if (!role) {
        setFetchError("Missing user role");
        setIsLoading(false);
        return;
      }

      try {
        const endpoint = role === "tutor" ? "tutor" : "student";
        const res = await axios.get(`${import.meta.env.VITE_API_BASE_URL}/users/profile/${endpoint}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setProfile({ ...res.data, role });
      } catch (err) {
        setFetchError("Failed to load profile");
      } finally {
        setIsLoading(false);
      }
    };

    fetchProfile();
  }, []);

  const renderTutorProfile = () => (
      <div className="space-y-2">
        <div><strong>Name:</strong> {profile.name}</div>
        <div><strong>Email:</strong> {profile.email}</div>
        <div><strong>Bio:</strong> {profile.bio}</div>
        <div><strong>Expertise:</strong> {profile.expertise}</div>
        <div><strong>Hourly Rate:</strong> ${profile.hourlyRate}</div>
        <div><strong>Years of Experience:</strong> {profile.yearsOfExperience}</div>
        <div><strong>Certifications:</strong> {profile.certifications}</div>
        <div><strong>Address:</strong> {profile.address}</div>
      </div>
  );

  const renderStudentProfile = () => (
      <div className="space-y-2">
        <div><strong>Name:</strong> {profile.name}</div>
        <div><strong>Email:</strong> {profile.email}</div>
        <div><strong>Education Level:</strong> {profile.educationLevel}</div>
        <div><strong>Subject Needed:</strong> {profile.subjectNeed}</div>
        <div><strong>Brief Description:</strong> {profile.briefDescription}</div>
        <div><strong>Address Area:</strong> {profile.addressArea}</div>
        <div><strong>Address:</strong> {profile.address}</div>
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
                  <p>{fetchError}</p>
              ) : (
                  <>
                    {profile.role === "tutor" && renderTutorProfile()}
                    {profile.role === "student" && renderStudentProfile()}

                    <div className="mt-6">
                      <Button onClick={() => {
                        if (profile.role === "tutor") navigate("/dashboard/profile/tutor");
                        if (profile.role === "student") navigate("/dashboard/profile/student");
                      }}>
                        Edit Profile
                      </Button>
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
