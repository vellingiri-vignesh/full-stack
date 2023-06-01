import {Button, ButtonGroup} from '@chakra-ui/react'
import SidebarWithHeader from "./shared/Sidebar.jsx";
import {useEffect} from "react";
import {getCustomers} from "./services/client.js";

const App = () => {

    useEffect(() => {
        getCustomers()
            .then(res => {
                console.log(res);
            })
            .catch(error => console.log(error));
    },[])
    // const customers =
    return (
        <SidebarWithHeader>
            <Button colorScheme='teal' variant='link'>My first button</Button>
        </SidebarWithHeader>
    );
}

export default App;