import ConstRoutes from "../../shared/contants/const-routes";
import {pages} from "../../pages.permissions";

export const menu = [
    { title: "Asignaturas", description: "Asignaturas del Profesor", navigation: ConstRoutes.PATH_TEACHER_SUBJECT, roles: pages[ConstRoutes.PATH_TEACHER_SUBJECT]},
    { title: "Asignaturas", description: "Asignaturas del Alumno", navigation: ConstRoutes.PATH_STUDENT_SUBJECT, roles: pages[ConstRoutes.PATH_STUDENT_SUBJECT]},
    { title: "Users", description: "Mantenimiento de Usuarios", navigation: ConstRoutes.PATH_USERS, roles: pages[ConstRoutes.PATH_USERS]},
    { title: "Asignaturas", description: "Mantenimiento de Asignaturas", navigation: ConstRoutes.PATH_SUBJECTS, roles: pages[ConstRoutes.PATH_SUBJECTS]},
    { title: "Tests", description: "Mantenimiento de Tests", navigation: ConstRoutes.PATH_TESTS, roles: pages[ConstRoutes.PATH_TESTS]},
    { title: "Nuevo Tests", description: "Creacion de Nuevo Test", navigation: ConstRoutes.PATH_NEW_TEST, roles: pages[ConstRoutes.PATH_NEW_TEST]},
    //{ title: "Preguntas", description: "Mantenimiento de Preguntas de tests", navigation: ConstRoutes.PATH_QUESTIONS, roles: pages[ConstRoutes.PATH_QUESTIONS]},
    { title: "Asignar Usuarios", description: "Asignar usuarios a asignaturas", navigation: ConstRoutes.PATH_ASSGN_USER_SUBJECT, roles: pages[ConstRoutes.PATH_ASSGN_USER_SUBJECT]}
  ];
