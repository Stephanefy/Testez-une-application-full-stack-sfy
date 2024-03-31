import { DebugElement } from "@angular/core";
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { By } from "@angular/platform-browser";
import { ActivatedRoute, Router } from "@angular/router";
import { SessionService } from "src/app/services/session.service";

export function findEl<T>(
    fixture: ComponentFixture<T>,
    testId: string
  ): DebugElement {
    return fixture.debugElement.query(
      By.css(`[data-testid="${testId}"]`)
    );
  }

/**
 * Makes a fake click event that provides the most important properties.
 * Sets the button to left.
 * The event can be passed to DebugElement#triggerEventHandler.
 *
 * @param target Element that is the target of the click event
 */
export function makeClickEvent(target: EventTarget): Partial<MouseEvent> {
  return {
    preventDefault(): void {},
    stopPropagation(): void {},
    stopImmediatePropagation(): void {},
    type: 'click',
    target,
    currentTarget: target,
    bubbles: true,
    cancelable: true,
    button: 0,
  };
}

/**
 * Emulates a left click on the element with the given `data-testid` attribute.
 *
 * @param fixture Component fixture
 * @param testId Test id set by `data-testid`
 */
export function click<T>(fixture: ComponentFixture<T>, testId: string): void {
  const element = findEl(fixture, testId);
  const event = makeClickEvent(element.nativeElement);
  element.triggerEventHandler('click', event);
}

// TODO: improve this configuration function to make it generic
export function configureTestingModule(sessionServiceMock: Partial<SessionService>, activatedRouteMock: Partial<ActivatedRoute>, component: any) {
    TestBed.overrideProvider(SessionService, { useValue: sessionServiceMock });
    TestBed.overrideProvider(ActivatedRoute, { useValue: activatedRouteMock });
    TestBed.compileComponents(); // Recompile the testing module with overrides

    const fixture = TestBed.createComponent(component);
    const angularComponent = fixture.componentInstance;
    const router = TestBed.inject(Router);
    
    return {
      fixture,
      angularComponent,
      router
    }
  }