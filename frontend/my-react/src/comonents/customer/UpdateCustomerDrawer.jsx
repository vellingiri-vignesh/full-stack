import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent,
    DrawerFooter,
    DrawerHeader,
    DrawerOverlay,
    useDisclosure
} from "@chakra-ui/react";
import UpdateCustomerForm from "./UpdateCustomerForm";

const CloseIcon = () => "x";
const UpdateCustomerDrawer = ({fetchCustomers, initialValues, customerId}) => {
    const { isOpen, onOpen, onClose } = useDisclosure()
    return <>

        <Button
            mb={8}
            bg={'gray.200'}
            color={'black'}
            rounded={'full'}
            _hover={{
                transform: 'translateY(-2px)',
                boxShadow: 'lg'
            }}
            _focus={{
                bg:  'gray.500'
            }}
            colorScheme='red'
            onClick={onOpen}>
            Update Customer
        </Button>
        <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
            <DrawerOverlay />
            <DrawerContent>
                <DrawerCloseButton />
                <DrawerHeader>Create new Customer</DrawerHeader>

                <DrawerBody>
                    <UpdateCustomerForm
                        fetchCustomers={fetchCustomers}
                        initialValues={initialValues}
                        customerId={customerId}
                    />
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
export default UpdateCustomerDrawer;