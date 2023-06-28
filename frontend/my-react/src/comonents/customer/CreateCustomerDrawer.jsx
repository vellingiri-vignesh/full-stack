import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent, DrawerFooter,
    DrawerHeader,
    DrawerOverlay, useDisclosure
} from "@chakra-ui/react";
import CreateCustomerForm from "./CreateCustomerForm";

const AddIcon = () => "+";
const CloseIcon = () => "x";
const CreateCustomerDrawer = ({fetchCustomers}) => {
    const { isOpen, onOpen, onClose } = useDisclosure()
    return <>
        <Button
            leftIcon={<AddIcon/>}
            colorScheme={"cyan"}
            onClick={onOpen}>
            Create Customer
        </Button>
        <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
            <DrawerOverlay />
            <DrawerContent>
                <DrawerCloseButton />
                <DrawerHeader>Create new Customer</DrawerHeader>

                <DrawerBody>
                    <CreateCustomerForm fetchCustomers={fetchCustomers}></CreateCustomerForm>
                </DrawerBody>

                <DrawerFooter>
                    <Button
                        leftIcon={<CloseIcon/>}
                        colorScheme={"cyan"}
                        onClick={onClose}>
                        Close
                    </Button>
                </DrawerFooter>
            </DrawerContent>
        </Drawer>
    </>
}
export default CreateCustomerDrawer;