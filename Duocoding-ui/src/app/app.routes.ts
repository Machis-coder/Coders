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
import {pages} from "./pages.permissions";
import {QuestionEditComponent} from "./features/question-edit-component/question-edit.component";
import {PerformTestComponent} from "./features/perform-test-component/perform-test.component";
import {
    PerformTestExecutionReviewComponent
} from "./features/perform-test-execution-review-component/perform-test-execution-review.component";

export const routes: Routes = [
    { path: '', redirectTo: ConstRoutes.PATH_LOGIN, pathMatch: 'full' },
    { path: ConstRoutes.PATH_LOGIN, component: LoginComponent },
    { path: ConstRoutes.PATH_HOME, component: HomeComponent },


    { path: ConstRoutes.PATH_TEACHER_SUBJECT, component: TeacherSignatureComponent, data: { roles: pages[ConstRoutes.PATH_TEACHER_SUBJECT]}},
    { path: ConstRoutes.PATH_TEST_REVIEW_TEACHER + '/:id', component: TestReviewTeacherComponent, data: {roles: pages[ConstRoutes.PATH_TEST_REVIEW_TEACHER] }},

    { path: ConstRoutes.PATH_STUDENT_SUBJECT, component: StudentSignatureComponent, data: { roles: pages[ConstRoutes.PATH_STUDENT_SUBJECT]}},
    {path: ConstRoutes.PATH_SIGNATURE_TEST + '/:subjectId', component: SignatureTestComponent, data: { roles: [pages[ConstRoutes.PATH_SIGNATURE_TEST]] }},
    {path: ConstRoutes.PATH_TEST_REVIEW_STUDENT + '/:id', component: TestReviewStudentComponent, data: {roles: pages[ConstRoutes.PATH_TEST_REVIEW_STUDENT] }},

    {path: ConstRoutes.PATH_ASSGN_USER_SUBJECT, component: AssignUserSubjectComponent, data: { roles: pages[ConstRoutes.PATH_ASSGN_USER_SUBJECT]}},

    { path: ConstRoutes.PATH_USERS, component: UsersComponent, data: { roles: pages[ConstRoutes.PATH_USERS]} },
    { path: ConstRoutes.PATH_USER, component: UserComponent, data: { roles: pages[ConstRoutes.PATH_USER]}},
    { path: ConstRoutes.PATH_SUBJECTS, component: SignaturesComponent, data: { roles: pages[ConstRoutes.PATH_SUBJECTS]}},
    { path: ConstRoutes.PATH_USER + '/:id', component: UserComponent, data: { roles: pages[ConstRoutes.PATH_USER]}},


    { path: ConstRoutes.PATH_TESTS, component: TestsComponent, data: { roles: pages[ConstRoutes.PATH_TESTS]}},
    { path: ConstRoutes.PATH_NEW_TEST, component: NewTestComponent, data: { roles: pages[ConstRoutes.PATH_NEW_TEST]}},
    { path: ConstRoutes.PATH_NEW_TEST + '/:id', component: NewTestComponent, data: { roles: pages[ConstRoutes.PATH_NEW_TEST]}},

    { path: ConstRoutes.PATH_PERFORM_TEST + '/:id', component: PerformTestComponent, data: { roles: pages[ConstRoutes.PATH_PERFORM_TEST]}},
    { path: ConstRoutes.PATH_TEST_REVIEW + '/:id', component: PerformTestExecutionReviewComponent, data: { roles: pages[ConstRoutes.PATH_TEST_REVIEW]}},

    { path: ConstRoutes.PATH_QUESTIONS, component: QuestionsComponent, data: { roles: pages[ConstRoutes.PATH_QUESTIONS]}},
    { path: ConstRoutes.PATH_QUESTION, component: QuestionEditComponent, data: { roles: pages[ConstRoutes.PATH_QUESTION]}},
    { path: ConstRoutes.PATH_QUESTION + '/:id', component: QuestionEditComponent, data: { roles: pages[ConstRoutes.PATH_QUESTION]}},

    { path: ConstRoutes.PATH_FORBIDDEN, component: ForbiddenComponent},

    { path: ConstRoutes.PATH_REGISTER, component: RegisterComponent }

];


