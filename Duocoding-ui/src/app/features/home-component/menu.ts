export const menu = [
    { title: "Asignaturas", description: "Asignaturas del Profesor", navigation: "teacher-subjects", roles: ["TEACHER"]},
    { title: "Asignaturas", description: "Asignaturas del Alumno", navigation: "student-subjects", roles: ["PUPIL"]},
    { title: "Users", description: "Mantenimiento de Usuarios", navigation: "users", roles: ["SUPERADMIN","ADMIN"]},
    { title: "Asignaturas", description: "Mantenimiento de Asignaturas", navigation: "signatures", roles: ["SUPERADMIN","ADMIN", "TEACHER"]},
    { title: "Tests", description: "Mantenimiento de Tests", navigation: "tests", roles: ["ADMIN", "TEACHER"]},
    { title: "Nuevo Tests", description: "Creacion de Nuevo Test", navigation: "new-test", roles: ["ADMIN", "TEACHER", "SUPERADMIN"]},
    { title: "Preguntas", description: "Mantenimiento de Preguntas de tests", navigation: "questions", roles: ["SUPERADMIN","ADMIN", "TEACHER"]},
    {title: "Asignar Usuarios", description: "Asignar usuarios a asignaturas", navigation: "assign-user-subject", roles: ["SUPERADMIN","ADMIN", "TEACHER"]}
  ];