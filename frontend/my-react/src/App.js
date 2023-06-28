import {Spinner, Text, Wrap, WrapItem} from '@chakra-ui/react'
import SidebarWithHeader from "./comonents/shared/Sidebar";
import {useEffect, useState} from "react";
import {getCustomers} from "./services/client";
import CardWithImage from "./comonents/CustomerCard";
import CreateCustomerDrawer from "./comonents/customer/CreateCustomerDrawer";
import {errorNotification} from "./services/notification";

function App() {

    const [customers, setCustomers] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [err, setErr] = useState("");
    const fetchCustomers = () => {
        setIsLoading(true);
        getCustomers()
            .then(res => {
                setCustomers(res.data);
            })
            .catch(err => {
                console.log(err);
                setErr(err.response?.data.message || 'Error fetching Customers');
                errorNotification(
                    err.code,
                    err.response?.data.message
                );
            })
            .finally(() => {
                setIsLoading(false);
            })
    }
    useEffect(() => {
        fetchCustomers();
    }, []);

    if (isLoading) {
        return (
            <SidebarWithHeader>
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color='blue.500'
                    size='xl'
                />
            </SidebarWithHeader>
        );
    }

    // TODO: avoid duplication of CreateCustomerDrawer

    if (err) {
        return (
            <SidebarWithHeader>
                <CreateCustomerDrawer fetchCustomers={fetchCustomers}/>
                <Text mt={5}>Oops there was an error !!!</Text>
            </SidebarWithHeader>
        );
    }

    if (customers.length <= 0) {
        return (
            <SidebarWithHeader>
                <CreateCustomerDrawer fetchCustomers={fetchCustomers}/>
                <Text mt={5}>No Customers Available</Text>
            </SidebarWithHeader>
        );
    }
    return (
        <SidebarWithHeader>
            <CreateCustomerDrawer fetchCustomers={fetchCustomers}/>
            <Wrap justify={'center'} spacing={"30px"}>
                {customers.map((customer, index) => {
                    return (
                        <WrapItem key={index}>
                            <CardWithImage {...customer} imageNumber={index} fetchCustomers={fetchCustomers}></CardWithImage>
                        </WrapItem>
                    )
                })}
            </Wrap>
        </SidebarWithHeader>
    );
}

export default App;
