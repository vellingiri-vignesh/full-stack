import axios from "axios";

const customersEndpoint = `${process.env.REACT_APP_BASE_URL}/api/v1/customers`
const loginEndpoint = `${process.env.REACT_APP_BASE_URL}/api/v1/auth/login`
const customerProfilePictureEndpoint = `https://randomuser.me/api/portraits/med`

const getAuthConfig = () => (
    {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('access_token')}`,
        }
    }
)
export const getProfilePictureUrl = (gender, id) => {
    const g = gender === "MALE" ? "men" : "women";
    return `${customerProfilePictureEndpoint}/${g}/${id}.jpg`;
}
export const getCustomers = async () => {
    try {
        return await axios.get(
            customersEndpoint,
            getAuthConfig()
        );
    } catch (e) {
        throw e;
    }
}

export const getCustomerById = async (customerId) => {
    try {
        return await axios.get(
            customersEndpoint.concat("/",customerId),
            getAuthConfig()
        );
    } catch (e) {
        throw e;
    }
}

export const saveCustomer = async (customer) => {
    try {
        return (await axios.post(customersEndpoint, customer));
    } catch (e) {
        throw e;
    }
}

export const updateCustomer = async (customerId, customer) => {
    try {
        return (await axios.put(customersEndpoint.concat("/", customerId), customer, getAuthConfig()));
    } catch (e) {
        throw e;
    }
}

export const deleteCustomer = async (customerId) => {
    try {
        return (await axios.delete(customersEndpoint.concat("/", customerId), getAuthConfig()))
    } catch (e) {
        throw e;
    }
}
export const postLogin = async (credentials) => {
    try {
        return (await axios.post(loginEndpoint, credentials))
    } catch (e) {
        throw e;
    }
}