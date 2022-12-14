import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ArticlesService, ArticleWithoutId} from "../../generated-sources/openapi";
import {ViewportScroller} from "@angular/common";
import {ImageCroppedEvent} from "ngx-image-cropper";
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-create-article',
  templateUrl: './create-article.component.html',
  styleUrls: ['./create-article.component.scss']
})
export class CreateArticleComponent implements OnInit {

  @ViewChild("myInput")
  myInputVariable: ElementRef;
  fileToUpload: FileList | null = null;
  articleForm: FormGroup;
  maxUploadSize = 4194304;

  error: Error;
  submitted = false;
  title = "";

  imageIds = [];
  previews: string[] = [];
  imgChangeEvt: any = '';
  cropImgPreview: any = '';
  fileToReturn: File = null;
  uploaded = false;
  pressed = false;
  display = "none";
  errorImage = "";


  constructor(private _formBuilder: FormBuilder, private articleService: ArticlesService, private scroll: ViewportScroller,
    private toastr: ToastrService) {
    this.articleForm = this._formBuilder.group({
      title: ["", [Validators.required]],
      summary: ["", [Validators.required]],
      text: ["", [Validators.required]],
      image: []
    });
  }

  ngOnInit(): void {
  }

  onCloseHandled() {
    this.display = "none";
  }

  onFileChange(event: any): void {
    if (event.target.files[0] == null) {
      this.cropImgPreview = null;
      this.imgChangeEvt = null;
      this.fileToReturn = null;
    } else {

      if (event.target.files[0].size > this.maxUploadSize) {
        this.toastr.warning("Maximum file size exceeded");
      } else {
        this.pressed = false;
        this.imgChangeEvt = event;
      }
    }
  }

  cropImg(e: ImageCroppedEvent) {
    this.cropImgPreview = e.base64;
    this.fileToReturn = this.base64ToFile(e.base64, this.imgChangeEvt.target.files[0]?.name);
    if (this.fileToReturn.size > this.maxUploadSize) {
      this.toastr.info("The cropped image size exceeds 4MB");
    }

  }

  imgLoad() {
    this.errorImage = null;
  }

  initCropper() {
    // init cropper
  }

  imgFailed() {
    this.errorImage = "Wrong format selected";
  }

  reset() {
    this.myInputVariable.nativeElement.value = null;
  }

  base64ToFile(data, filename) {

    const arr = data.split(',');
    const mime = arr[0].match(/:(.*?);/)[1];
    const bstr = atob(arr[1]);
    let n = bstr.length;
    const u8arr = new Uint8Array(n);

    while (n--) {
      u8arr[n] = bstr.charCodeAt(n);
    }

    return new File([u8arr], filename, {type: mime});
  }


  helpUpload() {


    this.articleService.imagesPost(this.fileToReturn, "response").subscribe({
      next: res => {
        const location = res.headers.get("location");
        const id = location.split("/").pop();
        this.imageIds.push(id);
        this.previews.push(this.cropImgPreview);
        this.uploaded = true;
        this.pressed = true;
        this.fileToReturn = null;
        this.reset();
        this.toastr.success("Successfully uploaded image!");
      },
      error: (error) => {
        if (error.status === 0 || error.status === 500) {
          this.toastr.error(error.message);
        } else {
          this.toastr.warning(error.error);
        }
      }
    });

  }


  async createArticle() {

    try {

      this.submitted = true;

      if (this.articleForm.valid && (this.fileToUpload === null || this.fileToUpload.length > 0)) {

        const form = this.articleForm.value;

        this.title = form.title;

        const article: ArticleWithoutId = {
          title: form.title,
          summary: form.summary,
          text: form.text,
          images: this.imageIds
        };


        this.articleService.articlesPost(article).subscribe({
          next: () => {
            this.imageIds = [];
            this.submitted = false;
            this.previews = [];
            this.reset();
            this.display = "none";
            this.articleForm.reset();
            this.scroll.scrollToPosition([0, 0]);
            this.toastr.success("Successfully created article!");

          },
          error: (error) => {
            this.imageIds = [];

            if (error.status === 0 || error.status === 500) {
              this.toastr.error(error.message);
            } else {
              this.toastr.warning(error.error);
            }
          }
        });

      }
    } catch (error) {
      this.error = error;
    }
  }

  removeImage(id: number) {

    this.imageIds.splice(id, 1);
    this.previews.splice(id, 1);
  }

  openModal() {
    this.display = "block";
  }

}
