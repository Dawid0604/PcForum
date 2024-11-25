export interface UserProfileDTO {
    encryptedId: string,
    nickname: string,
    avatar: string,
    rank: string,
    numberOfPosts: number,
    numberOfUpVotes: number,
    numberOfDownVotes: number
}