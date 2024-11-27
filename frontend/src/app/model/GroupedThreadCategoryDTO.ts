import { ThreadDTO } from "./ThreadDTO";
import { ThreadCategoryDTO } from "./ThreadCategoryDTO";

export interface GroupedThreadCategoryDTO {
    encryptedId: string,
    name: string,    
    newestThread: ThreadDTO,
    subCategories: ThreadCategoryDTO[]
}