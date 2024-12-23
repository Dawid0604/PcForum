import { UserProfileObservationDTO } from "./UserProfileObservationDTO";

export interface UserProfileObservationsDTO {
    nickname: string,
    avatar: string,
    encryptedId: string,
    observations: UserProfileObservationDTO[]
}