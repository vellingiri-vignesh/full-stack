import {
    AlertDialog,
    AlertDialogBody,
    AlertDialogContent,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogOverlay,
    Avatar,
    Box,
    Button,
    Center,
    Flex,
    Heading,
    Image,
    Stack,
    Tag,
    Text,
    useColorModeValue,
    useDisclosure,
} from '@chakra-ui/react';
import {deleteCustomer, getProfileImage, getProfileImageSecure} from "../../services/client";
import {useRef} from "react";
import {errorNotification, successNotification} from "../../services/notification";
import UpdateCustomerDrawer from "./UpdateCustomerDrawer";

export default function CardWithImage(props) {
    var gender = props.gender === 'MALE' ? 'men' : 'women'
    const name = props.name
    const age = props.age
    const email = props.email
    const cancelRef = useRef()
    const {
        isOpen: isOpenAlertDialog,
        onOpen: onOpenAlertDialog,
        onClose: onCloseAlertDialog
    } = useDisclosure()

    return (
        <Center py={6}>
            <Box
                maxW={'300px'}
                minW={'300px'}
                w={'full'}
                m={2}
                bg={useColorModeValue('white', 'gray.800')}
                boxShadow={'lg'}
                rounded={'md'}
                overflow={'hidden'}>
                <Image
                    h={'120px'}
                    w={'full'}
                    src={
                        'https://images.unsplash.com/photo-1612865547334-09cb8cb455da?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80'
                    }
                    objectFit={'cover'}
                />
                <Flex justify={'center'} mt={-12}>
                    <Avatar
                        size={'xl'}
                        src={
                            getProfileImage(props.id)
                        }
                        alt={'Author'}
                        css={{
                            border: '2px solid white',
                        }}
                    />
                </Flex>

                <Box p={6}
                     maxW={'300px'}>
                    <Stack spacing={0} align={'center'} mb={5}>
                        <Tag borderRadius={"full"}>
                            {props.id}
                        </Tag>
                        <Heading fontSize={'2xl'} fontWeight={500} fontFamily={'body'}>
                            {props.name}
                        </Heading>
                        <Text color={'gray.500'}>{props.email}</Text>
                        <Text color={'gray.500'}>{props.age} | {gender} </Text>
                    </Stack>
                </Box>
                <Stack direction={'row'} justify={'center'} spacing={6}>
                    <Stack>
                        <UpdateCustomerDrawer
                            initialValues={{name, email, age}}
                            customerId={props.id}
                            fetchCustomers={props.fetchCustomers}
                        />
                    </Stack>
                    <Stack>
                        <Button
                            bg={'red.400'}
                            color={'white'}
                            rounded={'full'}
                            _hover={{
                                transform: 'translateY(-2px)',
                                boxShadow: 'lg'
                            }}
                            _focus={{
                                bg:  'gray.500'
                            }}
                            colorScheme='red'
                            onClick={onOpenAlertDialog}>
                            Delete
                        </Button>
                        <AlertDialog
                            isOpen={isOpenAlertDialog}
                            leastDestructiveRef={cancelRef}
                            onClose={onCloseAlertDialog}
                        >
                            <AlertDialogOverlay>
                                <AlertDialogContent>
                                    <AlertDialogHeader fontSize='lg' fontWeight='bold'>
                                        Delete Customer
                                    </AlertDialogHeader>

                                    <AlertDialogBody>
                                        Are you sure you want to delete {props.name}? You can't undo this action afterwards.
                                    </AlertDialogBody>

                                    <AlertDialogFooter>
                                        <Button ref={cancelRef} onClick={onCloseAlertDialog}>
                                            Cancel
                                        </Button>
                                        <Button colorScheme='red' onClick={() => {
                                            deleteCustomer(props.id)
                                                .then(res => {
                                                    console.log(res);
                                                    successNotification('deleted', 'Customer deleted');
                                                    props.fetchCustomers();
                                                })
                                                .catch(err => {
                                                    console.log(err);
                                                    errorNotification(err.code, err.response?.data.message)
                                                })
                                                .finally(
                                                    onCloseAlertDialog()
                                                );

                                        }} ml={3}>
                                            Delete
                                        </Button>
                                    </AlertDialogFooter>
                                </AlertDialogContent>
                            </AlertDialogOverlay>
                        </AlertDialog>
                    </Stack>

                </Stack>
            </Box>
        </Center>
    );
}