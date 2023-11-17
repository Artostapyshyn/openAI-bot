import React, {ReactNode, useEffect, useState} from "react";
import {useDispatch} from "react-redux";
import Cookies from 'universal-cookie';
import {Oval} from "react-loader-spinner";
import {ENDPOINTS} from "./ENDPOINTS";

const cookies = new Cookies();

export type ProtectedRouterChildren = {
    children: ReactNode;
    failed?: ReactNode;
};

export const ProtectedRouter: React.FC<ProtectedRouterChildren> = ({
                                                                       children,
                                                                       failed,
                                                                   }) => {
    const [isAuthorized, setAuthorized] = useState<boolean | null>(null);
    const dispatch = useDispatch();

    const [updatedProfile, setUpdatedProfile] = useState<boolean>(false);

    useEffect(() => {
        const clientInfo = localStorage.getItem("client");
        const token = cookies.get("token");

        if (clientInfo && token) {
            const client = JSON.parse(clientInfo);
            dispatch({type: "LOAD_CLIENT", payload: client});
            setAuthorized(true);
        } else if (token) {
            (async () => {
                try {
                    const response = await fetch(ENDPOINTS.getUser, {
                        method: 'GET',
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": `Bearer ${token}`,
                        },
                    });

                    const data = await response.json();

                    if (data && data[0] && data[0].id) {
                        dispatch({type: "LOAD_CLIENT", payload: data[0]});
                        setAuthorized(true);
                        localStorage.setItem("client", JSON.stringify(data[0]));
                        setUpdatedProfile(true);
                    } else {
                        setAuthorized(false);
                    }
                } catch (e) {
                    setAuthorized(false);
                }
            })();
        } else {
            setAuthorized(false);
        }

    }, [dispatch, updatedProfile]);

    if (isAuthorized === null) {
        return (
            <div className="wrapper">
                <Oval
                    height={60}
                    width={60}
                    color="#E43050"
                    wrapperStyle={{}}
                    visible={true}
                    ariaLabel="oval-loading"
                    secondaryColor="rgba(228, 48, 80, 1)"
                    strokeWidth={2}
                    strokeWidthSecondary={2}
                />
            </div>
        );
    }

    return (
        <>
            {isAuthorized ? children : failed || 'Authorization failed'}
        </>
    );
};