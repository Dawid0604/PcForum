export interface ActivitySummaryDTO {
    users: ActivityUserDTO[]
}

export interface ActivityUserDTO {
    encryptedId: string,
    avatar: string,
    nickname: string,
    numberOfUpVotes: number
}