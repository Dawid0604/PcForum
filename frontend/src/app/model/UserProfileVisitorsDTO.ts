import { UserProfileDTO } from "./UserProfileDTO";
import { UserProfileVisitorDTO } from "./UserProfileVisitorDTO";

export interface UserProfileVisitorsDTO {
    nickname: string,
    avatar: string,
    encryptedId: string,
    visitors: UserProfileVisitorDTO[]
}