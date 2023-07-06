import {createContext, useContext, useEffect, useState} from "react";
import {postLogin as performLogin} from "../../services/client";
import jwtDecode from "jwt-decode";

const AuthContext = createContext({});

const AuthProvider = ({children}) => {

    const [customer, setCustomer] = useState(null);

    const setCustomerFromToken = () => {

        let token = localStorage.getItem('access_token')
        if(token) {
            const decodedToken = jwtDecode(token);
            console.log(decodedToken)
            setCustomer({
                username: decodedToken.sub,
                roles: decodedToken.scopes
            })
        }
    }

    useEffect(() => {
        setCustomerFromToken()
    },[])

    const login = async (credentials) => {
        return new Promise((resolve, reject) => {
            performLogin(credentials)
                .then(res => {
                    const jwtToken = res.headers["authorization"];
                    const decodedToken = jwtDecode(jwtToken);
                    setCustomer({
                        username: decodedToken.sub,
                        roles: decodedToken.scopes
                    })
                    localStorage.setItem("access_token", jwtToken);
                    resolve(res);
                })
                .catch(err => {
                    console.log(err);
                    reject(err);
                })
        })
    }

    const logout = () => {
        localStorage.removeItem('access_token');
        setCustomer(null);
    }

    const isCustomerAuthenticated = () => {
        const token = localStorage.getItem("access_token");
        if(!token) {
            return false;
        }
        const {exp: expiration} = jwtDecode(token);
        if (Date.now() > expiration * 1000) {
            logout()
            return false;
        }
        return true;
    }

    return (
        <AuthContext.Provider value={{
            customer,
            login,
            logout,
            isCustomerAuthenticated
        }}>
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth = () => useContext(AuthContext);

export default AuthProvider;

