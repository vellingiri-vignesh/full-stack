import {
    Heading,
    Avatar,
    Box,
    Center,
    Image,
    Flex,
    Text,
    Stack,
    useColorModeValue, Tag,
} from '@chakra-ui/react';
import {getProfilePictureUrl} from "../services/client";

export default function CardWithImage(props) {
    return (
        <Center py={6}>
            <Box
                maxW={'350px'}
                w={'full'}
                bg={useColorModeValue('white', 'gray.800')}
                boxShadow={'2xl'}
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
                            getProfilePictureUrl(props.gender, props.id)
                        }
                        alt={'Author'}
                        css={{
                            border: '2px solid white',
                        }}
                    />
                </Flex>

                <Box p={6}>
                    <Stack spacing={0} align={'center'} mb={5}>
                        <Tag borderRadius={"full"}>
                            {props.id}
                        </Tag>
                        <Heading fontSize={'2xl'} fontWeight={500} fontFamily={'body'}>

                            {props.name}
                        </Heading>
                        <Text color={'gray.500'}>{props.email}</Text>
                        <Text color={'gray.500'}>{props.age} | {props.gender} </Text>
                    </Stack>


                </Box>
            </Box>
        </Center>
    );
}