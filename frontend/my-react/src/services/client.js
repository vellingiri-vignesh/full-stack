import axios from "axios";

const customersEndpoint = `${process.env.REACT_APP_BASE_URL}/api/v1/customers`
const customerProfilePictureEndpoint = `https://randomuser.me/api/portraits/med`
export const getProfilePictureUrl = (gender, id) => {
    const g = gender == "MALE" ? "men" : "women";
    return `${customerProfilePictureEndpoint}/${g}/${id}.jpg`;
}
export const getCustomers = async () => {
    try{
        return await axios.get(customersEndpoint);
    }
    catch (e) {
        throw e;
    }
}
