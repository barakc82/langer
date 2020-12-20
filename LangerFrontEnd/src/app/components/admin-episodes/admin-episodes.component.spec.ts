import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminEpisodesComponent } from './admin-episodes.component';

describe('AdminEpisodesComponent', () => {
  let component: AdminEpisodesComponent;
  let fixture: ComponentFixture<AdminEpisodesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminEpisodesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminEpisodesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
