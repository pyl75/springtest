package com.excelib.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes("articleId")
public class FollowMeController {
    private final Logger logger = Logger.getLogger(FollowMeController.class);
    private final String[] sensitiveWords = new String[]{"k1","s2"};

    @ModelAttribute("comment")
    public String replaceSensitiveWords(String comment){
        if (comment!=null){
            logger.info("原始comment："+comment);
            for (String sw:sensitiveWords) {
                comment = comment.replaceAll(sw,"");
                logger.info("去敏感词后comment："+comment);
            }
        }
        return comment;
    }

    @RequestMapping(value = "articles/{articleId}/comment")
    public String doComment(@PathVariable String articleId, RedirectAttributes attributes, Model model){
        attributes.addFlashAttribute("comment",model.asMap().get("comment"));
        model.addAttribute("articleId",articleId);
        //此处将评论保存到数据库
        return "redirect:/showArticle";
    }

    @RequestMapping
    public String showArticle(Model model, SessionStatus sessionStatus){
        String articleId = (String) model.asMap().get("articleId");
        model.addAttribute("articleTitle",articleId+"号文章标题");
        model.addAttribute("articleId",articleId+"号文章内容");
        sessionStatus.setComplete();
        return "article";
    }
}
