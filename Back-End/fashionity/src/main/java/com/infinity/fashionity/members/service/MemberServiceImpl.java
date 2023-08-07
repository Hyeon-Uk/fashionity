package com.infinity.fashionity.members.service;

import com.infinity.fashionity.follows.entity.FollowEntity;
import com.infinity.fashionity.follows.entity.FollowKey;
import com.infinity.fashionity.follows.repository.FollowRepository;
import com.infinity.fashionity.global.utils.RegexUtil;
import com.infinity.fashionity.members.dto.*;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.exception.CustomValidationException;
import com.infinity.fashionity.members.exception.IdOrPasswordNotMatchedException;
import com.infinity.fashionity.members.exception.MemberNotFoundException;
import com.infinity.fashionity.members.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.infinity.fashionity.global.exception.ErrorCode.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ProfileDTO.Response getMemberProfile(Long seq, String nickname) {
        MemberEntity memberByNickname = memberRepository.findByNickname(nickname).orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));
        List<FollowEntity> followingList = followRepository.findByMember(memberByNickname);
        List<FollowEntity> followedList = followRepository.findByFollowedMember(memberByNickname);

        return ProfileDTO.Response.builder()
                .nickname(memberByNickname.getNickname())
                .profileUrl(memberByNickname.getProfileUrl())
                .profileIntro(memberByNickname.getProfileIntro())
                .followerCnt(followedList.size())
                .followingCnt(followingList.size())
                .myProfile(memberByNickname.getSeq().equals(seq))
                .build();
    }

    @Override
    public ProfilePostDTO.Response getMemberProfilePost(Long seq, String nickname) {
        return null;
    }

    @Override
    public ProfilePostDTO.Response getMemberProfileLikedPost(Long seq, String nickname) {
        return null;
    }

    @Override
    public ProfilePostDTO.Response getMemberProfileHiddenPost(Long seq, String nickname) {
        return null;
    }

    @Override
    @Transactional
    public ProfileDTO.Response editMemberProfile(Long seq, ProfileDTO.Request profile) {
        MemberEntity member = memberRepository.findById(seq).orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));
        List<FollowEntity> followingList = followRepository.findByMember(member);
        List<FollowEntity> followedList = followRepository.findByFollowedMember(member);

        member.updateProfile(profile);

        if (RegexUtil.checkNicknameRegex(profile.getNickname()))
            throw new CustomValidationException(INVALID_MEMBER_NICKNAME);

        return ProfileDTO.Response.builder()
                .nickname(member.getNickname())
                .profileUrl(member.getProfileUrl())
                .profileIntro(member.getProfileIntro())
                .followerCnt(followedList.size())
                .followingCnt(followingList.size())
                .myProfile(member.getSeq().equals(seq))
                .build();
    }

    @Override
    @Transactional
    public ProfileDTO.PwResponse editMyPassword(Long seq, ProfileDTO.PwRequest data) {
        MemberEntity member = memberRepository.findById(seq).orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(data.getPassword(), member.getPassword()))
            throw new IdOrPasswordNotMatchedException(CREDENTIAL_NOT_MATCHED);

        if (!RegexUtil.checkPasswordRegex(data.getNewPassword()))
            throw new CustomValidationException(INVALID_MEMBER_PASSWORD);

        member.setPassword(passwordEncoder.encode(data.getNewPassword()));
        return ProfileDTO.PwResponse.builder()
                .success(true)
                .build();
    }

    @Override
    public MemberFollowDTO.FollowingResponse getFollowings(Long seq, String nickname) {

        MemberEntity memberByNickname = memberRepository.findByNickname(nickname).orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));
        List<FollowEntity> followEntityList = followRepository.findByMember(memberByNickname);
        List<Following> followingList = new ArrayList<>();

        followEntityList.stream().forEach(e -> {
            MemberEntity followedMember = e.getFollowedMember();
            FollowKey followKey = FollowKey.builder()
                            .member(seq)
                            .followedMember(followedMember.getSeq())
                            .build();

            followingList.add(Following.builder()
                    .profileUrl(followedMember.getProfileUrl())
                    .nickname(followedMember.getNickname())
                    .isFollowing(followRepository.findById(followKey).isPresent())
                    .build());
        });
        return MemberFollowDTO.FollowingResponse.builder()
                .followings(followingList)
                .build();
    }

    @Override
    public MemberFollowDTO.FollowerResponse getFollowers(Long seq, String nickname) {

        MemberEntity memberByNickname = memberRepository.findByNickname(nickname).orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));
        List<FollowEntity> followEntityList = followRepository.findByFollowedMember(memberByNickname);
        List<Follower> followerList = new ArrayList<>();

        followEntityList.stream().forEach(e -> {
            MemberEntity followingMember = e.getMember();
            FollowKey followKey = FollowKey.builder()
                    .member(followingMember.getSeq())
                    .followedMember(seq)
                    .build();

            followerList.add(Follower.builder()
                    .profileUrl(followingMember.getProfileUrl())
                    .nickname(followingMember.getNickname())
                    .isFollowing(followRepository.findById(followKey).isPresent())
                    .build());
        });
        return MemberFollowDTO.FollowerResponse.builder()
                .followers(followerList)
                .build();
    }
}
