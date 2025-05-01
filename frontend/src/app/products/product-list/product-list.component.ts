import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Observable, BehaviorSubject, combineLatest, map } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  discountPrice?: number;
  rating: number;
  images: { imageUrl: string }[];
  category: { id: number; name: string };
}

interface Category {
  id: number;
  name: string;
}

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss']
})
export class ProductListComponent implements OnInit {
  // Ürünler ve kategoriler
  private products = new BehaviorSubject<Product[]>([]);
  products$: Observable<Product[]> = this.products.asObservable();
  categories$: Observable<Category[]>;
  
  // Filtreler
  selectedCategories: number[] = [];
  minPrice = 0;
  maxPrice = 10000;
  selectedPrice = this.maxPrice;
  
  // Sıralama
  sortOption = 'newest';
  
  // Sayfalama
  currentPage = 1;
  pageSize = 9;
  totalItems = 0;
  totalPages = 1;
  pageNumbers: number[] = [];
  
  constructor(private http: HttpClient) {
    // Kategorileri yükle
    this.categories$ = this.http.get<Category[]>(`${environment.apiUrl}/categories`);
  }
  
  ngOnInit(): void {
    this.loadProducts();
  }
  
  loadProducts(): void {
    // Gerçek uygulamada burada API'ye istek yapılacak
    // Şu an için örnek veri kullanıyoruz
    this.http.get<Product[]>(`${environment.apiUrl}/products`, {
      params: {
        page: this.currentPage.toString(),
        size: this.pageSize.toString(),
        sort: this.sortOption,
        maxPrice: this.selectedPrice.toString(),
        categories: this.selectedCategories.join(',')
      }
    }).subscribe(response => {
      // Gerçek API'de pagination bilgisi de dönecek
      this.products.next(response);
      this.totalItems = response.length; // Bu kısım gerçek API'ye göre düzenlenecek
      this.calculatePagination();
    });
  }
  
  onCategoryChange(event: Event): void {
    const checkbox = event.target as HTMLInputElement;
    const categoryId = Number(checkbox.value);
    
    if (checkbox.checked) {
      this.selectedCategories.push(categoryId);
    } else {
      this.selectedCategories = this.selectedCategories.filter(id => id !== categoryId);
    }
  }
  
  applyFilters(): void {
    this.currentPage = 1;
    this.loadProducts();
  }
  
  onSortChange(): void {
    this.loadProducts();
  }
  
  changePage(page: number): void {
    if (page < 1 || page > this.totalPages) {
      return;
    }
    
    this.currentPage = page;
    this.loadProducts();
  }
  
  private calculatePagination(): void {
    this.totalPages = Math.ceil(this.totalItems / this.pageSize);
    this.pageNumbers = Array.from({ length: this.totalPages }, (_, i) => i + 1);
  }
}
