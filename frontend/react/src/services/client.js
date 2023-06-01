import axios from "axios";

const customersEndpoint = `${process.env.REACT_APP_BASE_URL}/api/v1/customers`
export const getCustomers = async () => {
    try{
        return await axios.get(customersEndpoint);
    }
    catch (e) {
        throw e;
    }
}