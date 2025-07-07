import { getUser } from "../core/services/utils.service";
import { User } from "../interfaces/user";
import {UserResponseDTO} from "../interfaces/UserResponseDTO";

export const isUserLogged = () => {
    const user: UserResponseDTO = getUser()
    return user && user !== null;
}

export const userRole = () => {
    const user: UserResponseDTO = getUser()
    return user && user !== null? user.role: '' ;
}

export const isUserInRole = (roles: string[]) => {
    const userRol = userRole();
    return roles.length === 0 || userRol === '' || roles.indexOf(userRol) > -1;
}

export const userId = () => {
    const user = getUser();
    return user.id;
}
