import { ExtendedPostDTO } from "./ExtendedPostDTO";
import { ThreadDTO } from "./ThreadDTO";
import { UserProfileDTO } from "./UserProfileDTO";

export interface BrowserResultDTO {
    foundThreads: ThreadDTO[],
    foundPosts: ExtendedPostDTO[],
    foundUserProfiles: UserProfileDTO[]
}