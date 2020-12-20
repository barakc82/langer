import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminSentenceComponent } from './admin-sentence.component';

describe('AdminSentenceComponent', () => {
  let component: AdminSentenceComponent;
  let fixture: ComponentFixture<AdminSentenceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminSentenceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminSentenceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
