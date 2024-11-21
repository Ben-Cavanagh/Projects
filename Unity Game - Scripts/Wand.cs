// Include packages
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.InputSystem;

public class Wand : MonoBehaviour
{
    // Define input action variables
    private PlayerActions actions;
    private InputAction aimAction;
    private InputAction shootAction;

    // Define ray-related variables
    private Ray ray;
    private RaycastHit2D hit;
    [SerializeField] private new Camera camera;
    private Vector3 mousePosition;
    private Vector2 position;
    private Vector2 direction = Vector2.down;
    private float distance = float.PositiveInfinity;

    // Define ground layer
    [SerializeField] private LayerMask groundLayer;

    // Define rotation variables
    [SerializeField] private float turningRate = 500f;
    Vector3 targetDirection;
    Vector3 newDirection;

    // Define spellbolt variables
    [SerializeField] private Spellbolt spellbolt;
    [Range(1,40)] [SerializeField] private float spellboltSpeed = 30f;
    Spellbolt newSpellbolt;
    Vector3 spellboltDirection;

    // Define level manager
    [SerializeField] private LevelManager levelManager;

    private void Awake()
    {
        actions = new PlayerActions();
        aimAction = actions.Actions.aiming;
        shootAction = actions.Actions.shooting;
        shootAction.performed += OnShoot; 
    }

    private void OnEnable()
    {
        aimAction.Enable();
        shootAction.Enable();
    }

    private void OnDisable()
    {
        aimAction.Disable();
        shootAction.Disable();
    }

    private void OnShoot(InputAction.CallbackContext context)
    {
        // Get current mouse position in screen space
        mousePosition = GetMousePosition();

        if (mousePosition != null)
        {
            // Create new spellbolt
            newSpellbolt = Instantiate(spellbolt, transform.position, Quaternion.identity);
            newSpellbolt.LevelManager = levelManager;
            newSpellbolt.Speed = spellboltSpeed;

            // Calculate and set direction of spellbolt
            newSpellbolt.Direction = mousePosition - transform.position;
        }
    }

    private void Update()
    {
        // Rotate wand towards the mouse position
        mousePosition = GetMousePosition();
        if (mousePosition != null)
        {
            targetDirection = transform.position - mousePosition;
            newDirection = Vector3.RotateTowards(transform.forward, targetDirection, (turningRate * Time.deltaTime), 0.0f);
            transform.rotation = Quaternion.LookRotation(transform.forward, newDirection);
        }
    }

    private Vector3 GetMousePosition()
    {
        // Get current mouse position in screen space
        mousePosition = new Vector2(aimAction.ReadValue<Vector2>().x, aimAction.ReadValue<Vector2>().y);

        // Use raycasting to convert screen space to world space
        ray = camera.ScreenPointToRay(mousePosition);
        position = ray.origin;

        hit = Physics2D.Raycast(position, direction, distance, groundLayer);
        if (hit.collider != null)
        {
            mousePosition = hit.point;
        }
        return mousePosition;
    }
}
