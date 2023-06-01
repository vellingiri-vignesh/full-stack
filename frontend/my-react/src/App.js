import {Spinner, Text, Wrap, WrapItem} from '@chakra-ui/react'
import SidebarWithHeader from "./comonents/shared/Sidebar";
import {useEffect, useState} from "react";
import {getCustomers} from "./services/client";
import CardWithImage from "./comonents/Card";

function App() {

    const [customers, setCustomers] = useState([]);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {

        setIsLoading(true);
        getCustomers()
            .then(res => {
                setCustomers(res.data);
                setIsLoading(false);
            })
            .catch(error => {
                console.log(error);
            })
            .finally(() => {
                setIsLoading(false);
            })
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

    if (customers.length <= 0) {
        return (
            <SidebarWithHeader>
                <Text>No Customers Available</Text>
            </SidebarWithHeader>
        )
    }
    return (
        <SidebarWithHeader>
            <Wrap>
                {customers.map((customer, index) => {
                    return (
                        <WrapItem key={index}>
                            <CardWithImage {...customer}></CardWithImage>
                        </WrapItem>
                    )
                })}
            </Wrap>
        </SidebarWithHeader>
    );
}

export default App;
