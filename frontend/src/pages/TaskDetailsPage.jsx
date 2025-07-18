import React, { useEffect, useState } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import { useToast } from "@/components/ui/use-toast";
import { Calendar, DollarSign, BookOpen } from "lucide-react";
import { format } from "date-fns";

const TaskDetailsPage = () => {
    const { id } = useParams();
    const [task, setTask] = useState(null);
    const { toast } = useToast();

    useEffect(() => {
        axios
            .get(`${import.meta.env.VITE_API_BASE_URL}/tasks/${id}`)
            .then((res) => {
                console.log("API Response:", res.data); // Debug the response
                const taskData = res.data;
                setTask({
                    id: taskData.id || "",
                    title: taskData.title || "Untitled Task",
                    subject: taskData.subject || "Unknown",
                    budget: taskData.budget ? parseInt(taskData.budget) : "N/A",
                    deadline: taskData.deadline ? format(new Date(taskData.deadline), "yyyy-MM-dd") : "N/A",
                    address: taskData.address || "No address",
                    description: taskData.description || "No description",
                    status: taskData.status || "Unknown",
                    userId: taskData.userId || "Unknown", // Use userId from response
                    createdAt: taskData.createdAt ? format(new Date(taskData.createdAt), "yyyy-MM-dd") : "N/A",
                });
            })
            .catch((err) => {
                console.error("Failed to fetch task detail", err);
                toast({
                    title: "Error",
                    description: "Could not load task detail.",
                    variant: "destructive",
                });
            });
    }, [id, toast]);

    if (!task) {
        return <div className="p-6">Loading task details...</div>;
    }



    return (
        <div className="p-6 max-w-3xl mx-auto">
            <h1 className="text-2xl font-bold mb-4">{task.title}</h1>
            <p className="mb-2"><strong>Subject:</strong> {task.subject}</p>
            <p className="mb-2"><strong>Description:</strong> {task.description}</p>
            <p className="mb-2"><strong>Budget:</strong> ${task.budget}</p>
            <p className="mb-2"><strong>Deadline:</strong> {task.deadline}</p>
            <p className="mb-2"><strong>Address:</strong> {task.address}</p>
            <p className="mb-2"><strong>Status:</strong> {task.status}</p>
            <p className="mb-2"><strong>Created At:</strong> {task.createdAt}</p>
            <p className="mb-2"><strong>Posted By User ID:</strong> {task.userId}</p>
        </div>
    );
};
//
//     return (
//         <div className="max-w-3xl mx-auto p-6 bg-background text-foreground">
//             <h1 className="text-3xl font-bold mb-4">{task.title}</h1>
//             <div className="text-muted-foreground mb-2">
//                 Subject: <span className="font-semibold">{task.subject}</span>
//             </div>
//             <div className="flex items-center gap-4 mb-4 text-sm text-muted-foreground">
//                 <div className="flex items-center">
//                     <DollarSign className="h-4 w-4 mr-1" />
//                     Budget: {typeof task.budget === "number" ? `$${task.budget}` : task.budget}
//                 </div>
//                 <div className="flex items-center">
//                     <Calendar className="h-4 w-4 mr-1" />
//                     Deadline: {task.deadline}
//                 </div>
//                 <div className="flex items-center">
//                     <Calendar className="h-4 w-4 mr-1" />
//                     Posted: {task.createdAt}
//                 </div>
//             </div>
//             <div className="flex items-center mb-2">
//                 <BookOpen className="h-4 w-4 mr-2 text-blue-500" />
//                 <span className="text-sm text-muted-foreground">{task.address}</span>
//             </div>
//             <p className="text-base mt-4 leading-relaxed">{task.description}</p>
//         </div>
//     );
// };

export default TaskDetailsPage;