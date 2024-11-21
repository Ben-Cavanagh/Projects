// Include packages
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.InputSystem;

public class Player : MonoBehaviour
{
    // Define input action variables
    private PlayerActions actions;
    private InputAction movementAction;

    // Define movement and position-related variables
    [Range(1,10)] [SerializeField] private float speed = 5f;
    private float horizontalMovement;
    private float verticalMovement;
    private Vector2 startPosition = new Vector2(0,-1);

    // Define level manager
    [SerializeField] private LevelManager levelManager;

    private void Awake()
    {
        actions = new PlayerActions();
        movementAction = actions.Actions.movement;
    }

    private void OnEnable()
    {
        movementAction.Enable();
    }

    private void OnDisable()
    {
        movementAction.Disable();
    }

    private void Update()
    {
        // Read player movement input
        horizontalMovement = movementAction.ReadValue<Vector2>().x;
        verticalMovement = movementAction.ReadValue<Vector2>().y;

        // Check if a border is hit
        CheckBorderHit();

        // Move the player
        transform.Translate(Vector3.up * speed * verticalMovement * Time.deltaTime, Space.Self);
        transform.Translate(Vector3.right * speed * horizontalMovement * Time.deltaTime, Space.Self);

        // When level resets, set player position back to start
        if (levelManager.ResetLevel)
        {
            transform.position = startPosition;
        }
    }

    // Check if a border is hit
    private void CheckBorderHit()
    {
        if ((horizontalMovement < 0) && (transform.position.x <= levelManager.Borders[2]))
        {
            horizontalMovement = 0;
        }
        if ((horizontalMovement > 0) && (transform.position.x >= levelManager.Borders[3]))
        {
            horizontalMovement = 0;
        }
        if ((verticalMovement < 0) && (transform.position.y <= levelManager.Borders[1]))
        {
            verticalMovement = 0;
        }
        if ((verticalMovement > 0) && (transform.position.y >= levelManager.Borders[0]))
        {
            verticalMovement = 0;
        }
    }
}
