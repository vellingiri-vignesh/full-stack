import SidebarWithHeader from "./comonents/shared/Sidebar";
import {Text} from "@chakra-ui/react";

const Home = () => {

    return (
        <SidebarWithHeader>
            <Text fontSize={"4xl"}>Brewing...</Text>
        </SidebarWithHeader>
    )
}

export default Home;