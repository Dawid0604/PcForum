import { PostContentDTO } from "./PostContentDTO"
import { UserProfileDTO } from "./UserProfileDTO"

export interface PostDTO {
    encryptedId: string,
    user: UserProfileDTO,
    createdAt: string,
    content: PostContentDTO[],
    lastUpdatedAt: string,
    loggedUserPost: boolean,
    numberOfUpVotes: number,
    numberOfDownVotes: number,
    loggedUserHasUpVote: boolean,
    loggedUserHasDownVote: boolean
}