import Role from "./roles"
import ConstRoutes from "./shared/contants/const-routes"

export const pages = {
    [ConstRoutes.PATH_ROOT]: [],
    [ConstRoutes.PATH_LOGIN]: [],
    [ConstRoutes.PATH_HOME]: [],
    [ConstRoutes.PATH_TEACHER_SUBJECT]: [Role.TEACHER],
    [ConstRoutes.PATH_STUDENT_SUBJECT]: [Role.PUPIL],

    [ConstRoutes.PATH_SIGNATURE_TEST]: [Role.PUPIL],
    [ConstRoutes.PATH_USERS]: [Role.ADMIN, Role.SUPER],
    [ConstRoutes.PATH_USER]: [Role.ADMIN, Role.SUPER],
    [ConstRoutes.PATH_SUBJECTS]: [Role.TEACHER,Role.ADMIN, Role.SUPER],
    [ConstRoutes.PATH_TESTS]: [Role.TEACHER, Role.ADMIN, Role.SUPER],
    [ConstRoutes.PATH_NEW_TEST]: [Role.TEACHER, Role.ADMIN, Role.SUPER],
    [ConstRoutes.PATH_TEST_REVIEW_TEACHER]: [Role.TEACHER],
    [ConstRoutes.PATH_TEST_REVIEW_TEACHER]: [Role.TEACHER],
    [ConstRoutes.PATH_TEST_REVIEW_STUDENT]: [Role.PUPIL],

    [ConstRoutes.PATH_ASSGN_USER_SUBJECT]: [Role.ADMIN, Role.SUPER],



    [ConstRoutes.PATH_PERFORM_TEST]: [Role.PUPIL],
    [ConstRoutes.PATH_TEST_REVIEW]: [Role.PUPIL, Role.TEACHER,Role.ADMIN],
    [ConstRoutes.PATH_QUESTIONS]: [Role.TEACHER, Role.ADMIN, Role.SUPER],
    [ConstRoutes.PATH_QUESTION]: [Role.TEACHER, Role.ADMIN, Role.SUPER]
}
