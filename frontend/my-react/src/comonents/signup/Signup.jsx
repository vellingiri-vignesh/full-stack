import CreateCustomerForm from "../customer/CreateCustomerForm";
import {useNavigate} from "react-router-dom";
import {useAuth} from "../context/AuthContext";
import {useEffect} from "react";
import {Flex, Heading, Image, Link, Stack} from "@chakra-ui/react";

const Signup = () => {
    const navigate = useNavigate();
    const {customer, setCustomerFromToken } = useAuth();

    useEffect(() => {
        if(customer) {
            navigate("/dashboard")
        }
    })

    return (<Stack minH={'100vh'} direction={{base: 'column', md: 'row'}}>
        <Flex p={8} flex={1} align={'center'} justify={'center'}>
            <Stack spacing={4} w={'full'} maxW={'md'}>
                <Heading fontSize={'2xl'} mb={5}>Sign in to your account</Heading>
                <CreateCustomerForm onSuccess={(token) => {
                    localStorage.setItem("access_token", token);
                    setCustomerFromToken()
                    navigate("/dashboard")

                }}/>
                <Link color={"blue.500"} href={"/"}>
                    Have an account? Login now.
                </Link>

            </Stack>
        </Flex>
        <Flex flex={1}>
            <Image
                alt={'Login Image'}
                objectFit={'cover'}
                src={'https://as2.ftcdn.net/v2/jpg/06/14/35/79/1000_F_614357971_YgxMvjD3kB8brjOxPWWY0356ZirgcGzL.jpg'}
            />
        </Flex>
    </Stack>);
}

export default Signup;