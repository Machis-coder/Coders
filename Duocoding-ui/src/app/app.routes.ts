import { Routes } from '@angular/router';
import { LoginComponent } from './features/login/login.component';
import ConstRoutes from './shared/contants/const-routes';
import { HomeComponent } from './features/home-component/home-component';
import { UsersComponent } from './features/users-component/users.component';
import { ForbiddenComponent } from './features/forbidden-component/forbidden-component';
import { UserComponent } from './features/user-component/user.component';
import { TeacherSignatureComponent } from './features/teacher-signature-component/teacher-signature-component';
import { StudentSignatureComponent } from './features/student-signature-component/student-signature-component';
import { SignaturesComponent } from './features/signatures-component/signatures.component';
import { QuestionsComponent } from './features/questions-component/questions.component';
import { TestsComponent } from './features/tests-component/tests.component';
import { NewTestComponent } from './features/new-test-component/new-test.component';
import { RegisterComponent } from './features/register/register.component';
import {TestReviewTeacherComponent} from "./features/test-review-teacher/test-review-teacher.component";
import {SignatureTestComponent} from "./features/signature-test/signature-test.component";
import {TestReviewStudentComponent} from "./features/test-review-student/test-review-student.component";
import {AssignUserSubjectComponent} from "./features/assign-user-subject/assign-user-subject.component";



export const routes: Routes = [
    { path: '', redirectTo: ConstRoutes.PATH_LOGIN, pathMatch: 'full' },
    { path: ConstRoutes.PATH_LOGIN, component: LoginComponent },
    { path: 'home', component: HomeComponent },
    { path: 'teacher-subjects', component: TeacherSignatureComponent, data: { roles:["TEACHER"]}},
    {path: 'test-review-teacher/:id', component: TestReviewTeacherComponent,data:{roles:['TEACHER']}},

    { path: 'student-subjects', component: StudentSignatureComponent, data: { roles:["PUPIL"]}},
    {path: 'signature-test/:subjectId', component: SignatureTestComponent, data: { roles: ['PUPIL'] }},
    {path: 'test-review-student/:id', component: TestReviewStudentComponent, data: {roles: ['PUPIL'] }},

    {path: 'assign-user-subject', component: AssignUserSubjectComponent, data: { roles: ['ADMIN', 'SUPERADMIN']}},
    { path: 'users', component: UsersComponent, data: { roles:["ADMIN","SUPERADMIN"]} },
    { path: 'user', component: UserComponent, data: { roles:["ADMIN","SUPERADMIN"]}},
    { path: 'signatures', component: SignaturesComponent, data: { roles:["TEACHER", "ADMIN","SUPERADMIN"]}},
    { path: 'user/:id', component: UserComponent, data: { roles:["ADMIN","SUPERADMIN"]}},


    { path: 'tests', component: TestsComponent, data: { roles:["TEACHER", "ADMIN","SUPERADMIN"]}},
    { path: 'new-test', component: NewTestComponent, data: { roles:["TEACHER", "ADMIN","SUPERADMIN"]}},

    { path: 'questions', component: QuestionsComponent, data: { roles:["TEACHER", "ADMIN","SUPERADMIN"]}},

    { path: ConstRoutes.PATH_FORBIDDEN, component: ForbiddenComponent},
    { path: 'register', component: RegisterComponent },

];


