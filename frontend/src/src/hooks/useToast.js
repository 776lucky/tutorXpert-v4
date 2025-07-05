import { useContext, useCallback } from "react";
import { ToastContext } from "../contexts/ToastContext"; // Adjust path

export const useToast = () => {
    const context = useContext(ToastContext);

    if (!context) {
        throw new Error("useToast must be used within a ToastProvider");
    }

    const { addToast } = context;

    const toast = useCallback(
        (options) => {
            addToast(options);
        },
        [addToast]
    );

    return { toast };
};