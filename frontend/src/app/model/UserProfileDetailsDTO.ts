export interface UserProfileDetailsDTO {
    avatar: string,
    nickname: string,
    rank: string,
    numberOfPosts: number,
    numberOfThreads: number,
    numberOfUpVotes: number,
    numberOfDownVotes: number,
    numberOfVisits: number,
    numberOfFollowers: number,
    createdAt: string,
    lastActivity: string,
    activities: ActivityDTO[],
    isLoggedUser: boolean,
    isOnline: boolean
}

export interface ActivityDTO {
    title: string,
    description: string,
    date: string,
    encryptedId: string,
    type: ActivityType
}

export enum ActivityType {
    FOLLOW, CREATED_POST, COMMENTED_POST, VOTED_UP,
    VOTED_DOWN, CREATED_THREAD, COMMENTED_THREAD
}