import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import reportWebVitals from './reportWebVitals';
import {ChakraProvider, createStandaloneToast} from '@chakra-ui/react'
import {createBrowserRouter, RouterProvider, useRouteError} from "react-router-dom";
import Login from "./comonents/login/Login";
import AuthProvider from "./comonents/context/AuthContext";
import ProtectedRoute from "./comonents/shared/ProtectedRoute";
import Signup from "./comonents/signup/Signup";
import Home from "./Home";


const {ToastContainer} = createStandaloneToast()
const router = createBrowserRouter([
    {
        path: "/",
        element: <Login/>,
        errorElement: <ErrorBoundary/>
    },
    {
        path: "/signup",
        element: <Signup/>,
    },
    {
        path: "dashboard",
        element: <ProtectedRoute><Home/></ProtectedRoute>
    },
    {
        path: "dashboard/customers",
        element: <ProtectedRoute>
            <App/>
        </ProtectedRoute>
    }
])

function ErrorBoundary() {
    let error = useRouteError();
    console.error(error);
    // Uncaught ReferenceError: path is not defined
    return <div>Error loading the page!</div>;
}

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <ChakraProvider>
            <AuthProvider>
                <RouterProvider router={router}/>
            </AuthProvider>
            <ToastContainer/>
        </ChakraProvider>
    </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
