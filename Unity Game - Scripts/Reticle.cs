// Include packages
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.InputSystem;

[RequireComponent(typeof(SpriteRenderer))] 
public class Reticle : MonoBehaviour
{
    // Define input action variables
    private PlayerActions actions;
    private InputAction aimAction;

    // Define ray-related variables
    private Ray ray;
    private RaycastHit2D hit;
    [SerializeField] private new Camera camera;
    private Vector2 mousePosition;
    private Vector2 position;
    private Vector2 direction = Vector2.down;
    private float distance = float.PositiveInfinity;

    // Define ground layer for ray to hit
    [SerializeField] private LayerMask groundLayer;

    private void Awake()
    {
        actions = new PlayerActions();
        aimAction = actions.Actions.aiming;
    }

    private void OnEnable()
    {
        aimAction.Enable();
    }

    private void OnDisable()
    {
        aimAction.Disable();
    }

    private void Update()
    {
        // Get current mouse position
        mousePosition = new Vector2(aimAction.ReadValue<Vector2>().x, aimAction.ReadValue<Vector2>().y);

        // Create ray
        ray = camera.ScreenPointToRay(mousePosition);
        position = ray.origin;

        // Set reticle position to mouse position
        hit = Physics2D.Raycast(position, direction, distance, groundLayer);
        if (hit.collider != null)
        {
            transform.position = hit.point;
        }
    }
}
