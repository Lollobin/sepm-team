import {Component, OnInit} from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import {Article, ArticlesService, Sort} from "../../generated-sources/openapi";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  articles: Article[];
  error: Error;
  articleImages = {};
  empty = false;
  filterRead = null;
  defaultImage = 'https://dummyimage.com/640x360/fff/aaa';
  errorImage = 'https://mdbcdn.b-cdn.net/img/new/standard/city/053.webp';

  constructor(private articleService: ArticlesService, private toastr: ToastrService) {
  }

  ngOnInit() {
    this.getArticles();
  }

  getArticles() {
    this.articleService.articlesGet(false, 4, 0, Sort.Desc).subscribe({
      next: articlePage => {

        this.articles = articlePage.articles;
        this.empty = articlePage.articles.length === 0;
        for (const article of this.articles) {

          this.getImage(article.images[0], article.articleId);
          if (article.images?.length === 0) {
            this.articleImages[article.articleId] = this.errorImage;
          }
        }
        if (this.empty) {
          this.toastr.info("There are no new news!");
        }
      },
      error: (error) => {
        console.log(error);
        if (error.status === 0 || error.status === 500) {
          this.toastr.error(error.message);
        } else {
          this.toastr.warning(error.error);
        }
      }
    });
  }

  createImageFromBlob(image: Blob, id: number) {
    const reader = new FileReader();
    reader.addEventListener("load", () => {
      this.articleImages[id] = reader.result;

    }, false);

    if (image) {
      reader.readAsDataURL(image);
    }
  }


  getImage(id: number, articleId: number) {

    if (!id) {
      return;
    }

    this.articleService.imagesIdGet(id).subscribe({
      next: image => {
        this.createImageFromBlob(image, articleId);
      },
      error: (error) => {
        console.log(error);
        if (error.status === 0 || error.status === 500) {
          this.toastr.error(error.message);
        } else {
          this.toastr.warning(error.error);
        }
      }
    });
  }

  public vanishEmpty(): void {
    this.empty = null;
  }

}
