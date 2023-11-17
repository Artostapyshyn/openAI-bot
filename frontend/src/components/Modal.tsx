import React, { FC, ReactNode } from 'react';

interface ModalProps {
    onClose: () => void;
    children: ReactNode;
}

export const Modal: FC<ModalProps> = ({ onClose, children }) => {
    const handleOverlayClick = (e: React.MouseEvent<HTMLDivElement, MouseEvent>) => {
        if (e.target === e.currentTarget) {
            onClose();
        }
    };

    return (
        <div className="modal-overlay" onClick={handleOverlayClick}>
            <div className="modal-content">
                <button className="modal-close-button" onClick={onClose}>
                    Close
                </button>
                {children}
            </div>
        </div>
    );
};
