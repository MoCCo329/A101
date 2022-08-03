package a101.phorest.controller;
import a101.phorest.dto.PostDto;
import a101.phorest.jwt.TokenProvider;
import a101.phorest.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/community")
public class CommunityController {

    private final PostService postService;

    private final TokenProvider tokenProvider;

    @PostMapping("photogroup/like")
    @ResponseBody
    public List<PostDto> photoGroupLikeDownload(@RequestParam("limit") Long limit, @RequestParam("offset") Long offset, @RequestParam("humanCount") Long humanCount)
    {
        return postService.findByLikeCount("photogroup" ,limit, offset, humanCount);
    }

    @PostMapping("frame/like")
    @ResponseBody
    public List<PostDto> frameLikeDownload(@RequestParam("limit") Long limit, @RequestParam("offset") Long offset)
    {
        return postService.findByLikeCount("frame" ,limit, offset, 0L);
    }

    @GetMapping("{postId}")
    public PostDto getPost(@PathVariable("postId") Long postId, @RequestHeader(value = "Authorization", required = false) String token)
    {
        Optional<PostDto> postDto;
        if(token.isEmpty())
        {
            postDto = postService.findDtoOne(postId, "");
            return postDto.orElseGet(PostDto::new);
        }
        if(tokenProvider.validateToken(token))
            return new PostDto();
        String username = (String)tokenProvider.getTokenBody(token).get("username");
        postDto = postService.findDtoOne(postId, username);
        return postDto.orElseGet(PostDto::new);
    }
}